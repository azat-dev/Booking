package com.azat4dev.booking.listingsms;

import com.azat4dev.booking.listingsms.generated.client.api.CommandsModificationsApi;
import com.azat4dev.booking.listingsms.generated.client.api.QueriesPrivateApi;
import com.azat4dev.booking.listingsms.generated.client.base.ApiClient;
import com.azat4dev.booking.listingsms.generated.client.model.AddListingRequestBody;
import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.github.javafaker.Faker;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingsMsApplicationTests implements PostgresTests {

    private final static Faker faker = Faker.instance();
    @Autowired
    GenerateAccessToken generateAccessToken;
    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
    }

    @Test
    void test_addListing() throws UserId.WrongFormatException {
        // Given
        final var userId = UserId.checkAndMakeFrom(UUID.randomUUID().toString());

        // When
        givenExistingListing(userId);
    }

    @Test
    void test_getOnlyOwnListingDetails() throws UserId.WrongFormatException {

        // Given
        final var currentUserId = UserId.checkAndMakeFrom(UUID.randomUUID().toString());
        final var currentUserListing = givenExistingListing(currentUserId);

        final var currentUserAccessToken = generateAccessToken.execute(currentUserId);

        final var anotherUserId = UserId.checkAndMakeFrom(UUID.randomUUID().toString());
        final var anotherUserListing = givenExistingListing(anotherUserId);

        // When
        assertThrows(FeignException.Forbidden.class, () -> {
            apiClient(currentUserAccessToken, QueriesPrivateApi.class)
                .getListingPrivateDetails(anotherUserListing.id);
        });
    }

    ExistingListing givenExistingListing(UserId userId) {
        // Given
        final var accessToken = generateAccessToken.execute(userId);
        final var requestAddListing = anyRequestAddListing();

        // When
        final var body = apiClient(accessToken, CommandsModificationsApi.class)
            .addListing(requestAddListing);

        // Then
        final var listingId = body.getListingId();
        assertThat(listingId).isNotNull();

        final var response = apiClient(accessToken, QueriesPrivateApi.class)
            .getListingPrivateDetails(body.getListingId());

        assertThat(response.getListing().getId()).isEqualTo(body.getListingId());

        return new ExistingListing(listingId, userId.value());
    }

    // Helpers

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

    AddListingRequestBody anyRequestAddListing() {
        return new AddListingRequestBody()
            .operationId(UUID.randomUUID())
            .title(faker.book().title());
    }

    public interface GenerateAccessToken {
        String execute(UserId userId);
    }

    // Helpers

    record ExistingListing(
        UUID id,
        UUID userId
    ) {
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        RSAPrivateKey privateKey(@Value("${app.security.jwt.privateKey}") File file) throws Exception {
            final var stream = new FileInputStream(file);
            return RsaKeyConverters.pkcs8().convert(stream);
        }

        @Bean
        public JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
            final var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
            final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwks);
        }

        @Bean
        GenerateAccessToken generateAccessToken(JwtEncoder jwtEncoder) {
            return new GenerateAccessToken() {
                @Override
                public String execute(UserId userId) {
                    return jwtEncoder.encode(
                        JwtEncoderParameters.from(
                            JwtClaimsSet.builder()
                                .subject(userId.toString())
                                .claim("type", "access")
                                .expiresAt(Instant.now().plusSeconds(3600)
                                ).build()
                        )
                    ).getTokenValue();
                }
            };
        }
    }
}
