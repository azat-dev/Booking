package com.azat4dev.booking.shared.config.infrastracture.api;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ConnectBusApiEndpointsConfig.class})
public @interface AutoConnectApiEndpointsToBus {
}
