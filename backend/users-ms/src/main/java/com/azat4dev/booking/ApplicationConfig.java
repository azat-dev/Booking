package com.azat4dev.booking;

import com.azat4dev.booking.shared.application.GlobalControllerExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlobalControllerExceptionHandler.class)
public class ApplicationConfig {


}
