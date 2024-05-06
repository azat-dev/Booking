package com.azat4dev.demobooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@EnableAspectJAutoProxy
@Import(ThrowValidationAdvice.class)
@SpringBootApplication
public class DemobookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemobookingApplication.class, args);
    }

}
