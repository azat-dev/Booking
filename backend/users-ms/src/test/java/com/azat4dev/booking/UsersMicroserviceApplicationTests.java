package com.azat4dev.booking;

import com.azat4dev.booking.helpers.EmailBoxMock;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.*;
import com.azat4dev.booking.users.users_queries.presentation.api.rest.dto.PersonalUserInfoDTO;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersMicroserviceApplicationTests implements KafkaTests, PostgresTests {

    public static final Faker faker = Faker.instance();

    @Autowired
    EmailService emailService;

    @Autowired
    EmailBoxMock emailBox;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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

    SignUpRequest anySignUpRequest() {
        return new SignUpRequest(
            new FullNameDTO(
                faker.name().firstName(),
                faker.name().lastName()
            ),
            faker.internet().emailAddress(),
            faker.internet().password(Password.MIN_LENGTH, Password.MAX_LENGTH)
        );
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

    String anyIdempotentOperationId() {
        return UUID.randomUUID().toString();
    }

    @Test
    void test_resetPasswordByEmail() throws Exception {
        // Given
        final var user = givenAnySignedUpUser();
        final var request = new ResetPasswordByEmailRequest(
            anyIdempotentOperationId(),
            user.email().getValue()
        );

        emailBox.clearFor(user.email());

        // When
        performRequestResetPassword(request);

        // Then
        final var email = emailBox.waitFor(10, item -> {
            return item.email().equals(user.email());
        }).orElseThrow();

        //Given
        final var resetPasswordLink = parseLink(email.data().body().value());
        final var newPassword = anyPassword();

        //When
        performResetPassword(resetPasswordLink, newPassword);

        //Then
        final var userInfo = performGetNewTokensByEmail(user.email.getValue(), newPassword);
        assertThat(userInfo.tokens().access()).isNotNull();
    }

    SignedUpUser givenAnyConfirmedUser() throws Exception {
        // Given
        final var signedUpUser = givenAnySignedUpUser();

        // When
        confirmEmail(signedUpUser.verificationLink);

        // Then
        final var userInfo = performGetCurrentUser(signedUpUser.accessToken);

        // Then
        assertThat(userInfo.emailVerficationStatus()).isEqualTo("VERIFIED");
        return signedUpUser;
    }

    private SignedUpUser givenAnySignedUpUser() throws Exception {

        final var request = anySignUpRequest();
        final var email = EmailAddress.dangerMakeWithoutChecks(request.email());

        // When
        final var response = performSignUpRequest(request);

        // Then
        assertThat(response.tokens().access()).isNotNull();

        // When
        final var userInfo = performGetCurrentUser(response.tokens().access());

        // Then
        assertThat(userInfo.email()).isEqualTo(email.getValue());

        final var lastEmail = emailBox.waitFor(10, item -> {
            return item.email().equals(email);
        }).orElseThrow();

        final var emailBody = lastEmail.data().body().value();
        final var confirmationLink = parseLink(emailBody);

        return new SignedUpUser(
            UserId.checkAndMakeFrom(userInfo.id()),
            EmailAddress.checkAndMakeFromString(userInfo.email()),
            Password.checkAndMakeFromString(request.password()),
            response.tokens().access(),
            confirmationLink
        );
    }

    HttpHeaders headersWithToken(String token) {
        final var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String baseURL() {
        return "http://localhost:" + port;
    }

    private void confirmEmail(String confirmationLink) {
        try {
            final var query = URI.create(confirmationLink).toURL().getQuery();
            final var url = baseURL() + "/api/public/verify-email" + "?" + query;
            final var response = restTemplate.getForEntity(url, String.class);
            System.out.println(response.getBody());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void performResetPassword(String resetPasswordLink, String newPassword) {
        final var token = UriComponentsBuilder.fromUriString(resetPasswordLink)
            .build().getQueryParams().get("token").getFirst();

        final var url = baseURL() + "/api/public/set-new-password";
        final var response = restTemplate.postForObject(
            url,
            new CompleteResetPasswordRequest(
                anyIdempotentOperationId(),
                newPassword,
                token
            ),
            String.class
        );

        System.out.println(response);
    }

    private LoginByEmailResponse performGetNewTokensByEmail(String email, String password) {

        final var url = baseURL() + "/api/public/auth/token";

        return restTemplate.postForObject(
            url,
            new LoginByEmailRequest(
                email,
                password
            ),
            LoginByEmailResponse.class
        );
    }

    private PersonalUserInfoDTO performGetCurrentUser(String accessToken) {

        final var url = baseURL() + "/api/with-auth/users/current";
        final var headers = headersWithToken(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<PersonalUserInfoDTO> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            PersonalUserInfoDTO.class
        );

        return response.getBody();
    }

    private SignUpResponse performSignUpRequest(SignUpRequest request) throws Exception {
        return restTemplate.postForObject(
            baseURL() + "/api/public/auth/sign-up",
            request,
            SignUpResponse.class
        );
    }

    private String performRequestResetPassword(ResetPasswordByEmailRequest request) throws Exception {
        return restTemplate.postForObject(
            baseURL() + "/api/public/reset-password",
            request,
            String.class
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
