package com.azat4dev.booking.searchlistingsms;

import com.azat4dev.booking.searchlistingsms.helpers.EnableTestcontainers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchListingsDetailsServiceMsApplicationTests {

	@Test
	void contextLoads() {
	}

	@AfterAll
	static void tearDown() {

		System.out.println("Tearing down...");
	}

}
