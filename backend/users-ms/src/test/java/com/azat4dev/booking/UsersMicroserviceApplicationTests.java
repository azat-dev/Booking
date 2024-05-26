package com.azat4dev.booking;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    @MockBean
    EmailService emailService;

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
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
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
        final var email = request.email();

        // When
        final var response = performSignUpRequest(request);

        // Then
        then(emailService).should(times(1))
            .send(any(), any());
        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, SECONDS)
            .untilAsserted(() -> {
                then(emailService).should(times(1))
                    .send(any(), any());
            });


        final var userInfo = performGetCurrentUser(response.tokens().access());

        // Then
        assertThat(userInfo.email()).isEqualTo(email);
    }

    private void givenSignedUpUser() throws Exception {
        // Given
        final var request = anySignUpRequest();
        final var email = request.email();

        // When
        final var response = performSignUpRequest(request);

        // Then

        then(emailService).should(times(1))
            .send(any(), any());
        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, SECONDS)
            .untilAsserted(() -> {
                then(emailService).should(times(1))
                    .send(any(), any());
            });
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

}
