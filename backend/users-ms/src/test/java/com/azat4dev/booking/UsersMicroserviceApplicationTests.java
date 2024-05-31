package com.azat4dev.booking;

import com.azat4dev.booking.helpers.EmailBoxMock;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.booking.usersms.generated.client.api.*;
import com.azat4dev.booking.usersms.generated.client.base.ApiClient;
import com.azat4dev.booking.usersms.generated.client.model.*;
import com.github.javafaker.Faker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersMicroserviceApplicationTests implements KafkaTests, PostgresTests {

    public static final Faker faker = Faker.instance();

    @Autowired
    EmailBoxMock emailBox;

    @LocalServerPort
    private int port;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void contextLoads() {
    }

    SignUpByEmailRequestBody anySignUpRequest() {
        return new SignUpByEmailRequestBody()
            .fullName(
                new FullNameDTO()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
            ).email(faker.internet().emailAddress())
            .password(faker.internet().password(Password.MIN_LENGTH, Password.MAX_LENGTH));
    }

    private String anyPassword() {
        return faker.internet().password(Password.MIN_LENGTH, Password.MAX_LENGTH);
    }

    @Test
    void test_verifyEmail() throws Exception {
        // Given
        final var user = givenAnyConfirmedUser();

        // Then
        assertThat(user).isNotNull();
    }

    UUID anyIdempotentOperationId() {
        return UUID.randomUUID();
    }

    @Test
    void test_resetPasswordByEmail() throws Exception {
        // Given
        final var commandsApiClient = anonymousClient(CommandsResetPasswordApi.class);

        final var user = givenAnySignedUpUser();
        final var request = new ResetPasswordByEmailRequestBody()
            .operationId(anyIdempotentOperationId())
            .email(user.email().getValue());

        emailBox.clearFor(user.email());

        // When
        commandsApiClient.resetPasswordByEmail(request);

        // Then
        final var email = emailBox.waitFor(10, item -> {
            return item.email().equals(user.email());
        }).orElseThrow();

        //Given
        final var resetPasswordLink = parseLink(email.data().body().value());
        final var newPassword = anyPassword();
        final var token = parseParamFromLink(resetPasswordLink, "token");

        //When
        commandsApiClient.completeResetPassword(
            new CompleteResetPasswordRequestBody()
                .operationId(anyIdempotentOperationId())
                .resetPasswordToken(token)
                .newPassword(newPassword)
        );

        //Then
        final var loginResponse = anonymousClient(CommandsLoginApi.class).loginByEmail(
            new LoginByEmailRequestBody()
                .email(user.email().getValue())
                .password(newPassword)
        );

        assertThat(loginResponse.getTokens().getAccess()).isNotNull();
    }

    SignedUpUser givenAnyConfirmedUser() throws Exception {
        // Given
        final var signedUpUser = givenAnySignedUpUser();
        final var queriesApiClient = apiClient(signedUpUser.accessToken, QueriesCurrentUserApi.class);

        final var token = parseParamFromLink(signedUpUser.verificationLink, "token");
        // When
        final var response = anonymousClient(CommandsEmailVerificationApi.class)
            .verifyEmail(token);

        // Then
        final var userInfo = queriesApiClient.getCurrentUser();

        // Then
        assertThat(userInfo.getEmailVerificationStatus())
            .isEqualTo(EmailVerificationStatusDTO.VERIFIED);

        return signedUpUser;
    }

    private SignedUpUser givenAnySignedUpUser() throws Exception {

        final var request = anySignUpRequest();
        final var email = EmailAddress.dangerMakeWithoutChecks(request.getEmail());

        // When
        final var response = anonymousClient(CommandsSignUpApi.class)
            .signUpByEmail(request);

        // Then
        String accessToken = response.getTokens().getAccess();
        assertThat(accessToken).isNotNull();

        // When
        final var userInfo = apiClient(accessToken, QueriesCurrentUserApi.class)
            .getCurrentUser();

        // Then
        assertThat(userInfo.getEmail()).isEqualTo(email.getValue());

        final var lastEmail = emailBox.waitFor(10, item -> {
            return item.email().equals(email);
        }).orElseThrow();

        final var emailBody = lastEmail.data().body().value();
        final var confirmationLink = parseLink(emailBody);

        return new SignedUpUser(
            UserId.checkAndMakeFrom(userInfo.getId().toString()),
            EmailAddress.checkAndMakeFromString(userInfo.getEmail()),
            Password.checkAndMakeFromString(request.getPassword()),
            response.getTokens().getAccess(),
            confirmationLink
        );
    }

    private String parseLink(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);

        // Extract links
        Elements links = doc.select("a[href]");

        // Ensure there's only one link
        assertThat(links.size()).isEqualTo(1);

        // Get the href attribute of the link
        Element link = links.get(0);
        return link.attr("href");
    }

    private String parseParamFromLink(String link, String paramName) {
        return UriComponentsBuilder.fromUriString(link)
            .build().getQueryParams().getFirst(paramName);
    }

    private <T extends ApiClient.Api> T anonymousClient(Class<T> apiClass) {
        final var api = new ApiClient();
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }

    private <T extends ApiClient.Api> T apiClient(String accessToken, Class<T> apiClass) {
        final var api = new ApiClient("BearerAuth");
        api.setBearerToken(accessToken);
        api.setBasePath("http://localhost:" + port);
        return api.buildClient(apiClass);
    }

    record SignedUpUser(
        UserId userId,
        EmailAddress email,
        Password password,
        String accessToken,
        String verificationLink
    ) {
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        EmailBoxMock emailBox() {
            return new EmailBoxMock();
        }

        @Bean
        @Primary
        public EmailService emailServiceTest(EmailBoxMock emailBox) {

            return new EmailService() {
                @Override
                public void send(EmailAddress email, EmailData data) {
                    emailBox.add(email, data);
                }
            };
        }
    }
}
