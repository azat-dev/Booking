package com.azat4dev.booking.listingsms;

import com.azat4dev.booking.listingsms.helpers.PostgresTests;
import com.azat4dev.booking.listingsms.presentation.api.dto.AddListingRequest;
import com.azat4dev.booking.listingsms.presentation.api.dto.AddListingResponse;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListingsMsApplicationTests implements PostgresTests {

	private final static Faker faker = Faker.instance();

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	void contextLoads() {
	}

	AddListingRequest anyRequestAddListing() {
		return new AddListingRequest(
			UUID.randomUUID().toString(),
			faker.book().title(),
			faker.lorem().toString()
		);
	}

	@Test
	void test_addListing() {
		// Given
		final var requestAddListing = anyRequestAddListing();

		// When
		performAddNewListing(requestAddListing, "accessToken");

		// Then
	}

	private String baseURL() {
		return "http://localhost:" + port;
	}

	HttpHeaders headersWithToken(String token) {
		final var headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	AddListingResponse performAddNewListing(AddListingRequest addListingRequest, String accessToken) {

		final var url = baseURL() + "/api/listings";
		final var headers = headersWithToken(accessToken);

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

		final var response = restTemplate.exchange(
			url,
			HttpMethod.POST,
			requestEntity,
			AddListingResponse.class
		);

		return response.getBody();
	}

}
