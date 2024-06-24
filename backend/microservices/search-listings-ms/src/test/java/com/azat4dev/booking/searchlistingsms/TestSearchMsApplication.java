package com.azat4dev.booking.searchlistingsms;

import org.springframework.boot.SpringApplication;

public class TestSearchMsApplication {

	public static void main(String[] args) {
		SpringApplication.from(SearchListingsMsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
