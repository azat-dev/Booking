package com.azat4dev.booking.e2e;

import com.azat4dev.booking.helpers.EmailBoxMock;
import com.azat4dev.booking.helpers.EnableTestcontainers;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import com.azat4dev.booking.usersms.generated.client.api.*;
import com.azat4dev.booking.usersms.generated.client.model.*;
import net.datafaker.Faker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.azat4dev.booking.helpers.ApiHelpers.apiClient;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureObservability
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/db/drop-schema.sql", "/db/schema.sql"})
class UsersMicroserviceApplicationTests {

    public static final Faker faker = new Faker();

    @Autowired
    EmailBoxMock emailBox;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
    }

    SignUpByEmailRequestBodyDTO anySignUpRequest() {
        return new SignUpByEmailRequestBodyDTO()
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
    void test_verifyEmail(
        @Value("${spring.kafka.bootstrap-servers}")
        String bootstrapServers
    ) throws Exception {
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
        final var commandsApiClient = apiClient(CommandsResetPasswordApi::new, port);

        final var user = givenAnySignedUpUser();
        final var request = new ResetPasswordByEmailRequestBodyDTO()
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
            new CompleteResetPasswordRequestBodyDTO()
                .operationId(anyIdempotentOperationId())
                .resetPasswordToken(token)
                .newPassword(newPassword)
        );

        //Then
        final var loginResponse = apiClient(CommandsLoginApi::new, port)
            .loginByEmail(
                new LoginByEmailRequestBodyDTO()
                    .email(user.email().getValue())
                    .password(newPassword)
            );

        assertThat(loginResponse.getTokens().getAccess()).isNotNull();
    }

    @Test
    void test_fetchDataFromPrometheus_givenAuthenticated() {

        // Given
        final var url = "http://localhost:" + port + "/actuator/prometheus";

        // When
        final var response = restTemplateBuilder
            .basicAuthentication("prometheus-user", "prometheus-password")
            .build()
            .getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    SignedUpUser givenAnyConfirmedUser() throws Exception {
        // Given
        final var signedUpUser = givenAnySignedUpUser();
        final var queriesApiClient = apiClient(QueriesCurrentUserApi::new, signedUpUser.accessToken, port);

        final var token = parseParamFromLink(signedUpUser.verificationLink, "token");
        // When
        final var response = apiClient(CommandsEmailVerificationApi::new, port)
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
        final var email = request.getEmail();

        // When
        final var response = apiClient(CommandsSignUpApi::new, port)
            .signUpByEmail(request);

        // Then
        String accessToken = response.getTokens().getAccess();
        assertThat(accessToken).isNotNull();

        // When
        final var userInfo = apiClient(QueriesCurrentUserApi::new, accessToken, port)
            .getCurrentUser();

        // Then
        assertThat(userInfo.getEmail()).isEqualTo(email);

        System.out.println("Wait for Email = " + email);
        final var lastEmail = emailBox.waitFor(10, item -> {
            return item.email().getValue().equals(email);
        }).orElseThrow(
            () -> {
                return new RuntimeException("Email not found");
            }
        );

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
