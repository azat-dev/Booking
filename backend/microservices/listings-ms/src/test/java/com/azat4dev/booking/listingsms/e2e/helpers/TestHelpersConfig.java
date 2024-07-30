package com.azat4dev.booking.listingsms.e2e.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

@Import({AccessTokenConfig.class, ListingHelpers.class, PhotoHelpers.class})
@TestConfiguration
public class TestHelpersConfig {

    @Bean
    File testImageFile(
        @Value("classpath:/test_image.jpg")
        Resource testImageResource
    ) throws IOException {
        return testImageResource.getFile();
    }
}
