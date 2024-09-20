package com.azat4dev.booking.shared.helpers;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = KafkaTestContainerInitializer.class)
public @interface EnableTestcontainers {

    Class<?>[] classes() default {};
}