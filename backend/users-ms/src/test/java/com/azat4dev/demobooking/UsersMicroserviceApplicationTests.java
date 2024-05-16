package com.azat4dev.demobooking;

import com.azat4dev.demobooking.common.presentation.GlobalControllerExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(GlobalControllerExceptionHandler.class)
class UsersMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }

}
