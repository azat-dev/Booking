package com.azat4dev.booking.searchlistingsms;

import com.azat4dev.booking.searchlistingsms.helpers.EnableTestcontainers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@EnableTestcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchListingsDetailsServiceMsApplicationTests {

	@Test
	void contextLoads() {
	}

}
