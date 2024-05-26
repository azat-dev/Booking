package com.azat4dev.booking;

import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.SignUpResponse;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UsersMicroserviceApplicationTests {

    public static final Faker faker = Faker.instance();

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
    );

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:15-alpine"
    );

    @Autowired
    EmailService emailService;

    @Autowired
    TestConfig.EmailBox emailBox;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @DynamicPropertySource
    static void configurePropertiesKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

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

    @Test
    void test_signUp_User() throws Exception {
        // Given
        final var request = anySignUpRequest();
        final var email = EmailAddress.dangerMakeWithoutChecks(request.email());

        // When
        final var response = performSignUpRequest(request);

        // Then
        final var lastEmail = emailBox.waitFor(10, item -> {
            return item.email().equals(email);
        }).orElseThrow();

        final var emailBody = lastEmail.data().body().value();
        final var confirmationLink = parseLink(emailBody);
        assertThat(confirmationLink).isNotNull();

        confirmEmail(confirmationLink);

        final var userInfo = performGetCurrentUser(response.tokens().access());

        // Then
        assertThat(userInfo.email()).isEqualTo(email.getValue());
        assertThat(userInfo.emailVerficationStatus()).isEqualTo("VERIFIED");
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

    @TestConfiguration
    static class TestConfig {

        @Bean
        EmailBox emailBox() {
            return new EmailBox();
        }

        @Bean
        @Primary
        public EmailService emailServiceTest(EmailBox emailBox) {
            return new EmailService() {
                @Override
                public void send(EmailAddress email, EmailData data) {
                    emailBox.add(email, data);
                }
            };
        }

        public record EmailBoxItem(
            LocalDateTime capturedAt,
            EmailAddress email,
            EmailService.EmailData data
        ) {
        }

        public static class EmailBox {
            private final List<EmailBoxItem> items = new ArrayList<>();

            public void add(EmailAddress email, EmailService.EmailData body) {
                items.add(new EmailBoxItem(LocalDateTime.now(), email, body));
            }

            public EmailBoxItem last() {
                return items.get(items.size() - 1);
            }

            public void clear() {
                items.clear();
            }

            public Optional<EmailBoxItem> lastFor(Predicate<EmailBoxItem> predicate) {
                for (int i = items.size() - 1; i >= 0; i--) {

                    EmailBoxItem item = items.get(i);

                    if (predicate.test(item)) {
                        return Optional.of(item);
                    }
                }
                return Optional.empty();
            }

            public Optional<EmailBoxItem> waitFor(int seconds, Predicate<EmailBoxItem> predicate) {
                await()
                    .pollInterval(Duration.ofMillis(200))
                    .atMost(seconds, SECONDS)
                    .until(() -> {
                        final var isPresent = lastFor(predicate).isPresent();
                        return isPresent;
                    });

                return lastFor(predicate);
            }
        }
    }
}
