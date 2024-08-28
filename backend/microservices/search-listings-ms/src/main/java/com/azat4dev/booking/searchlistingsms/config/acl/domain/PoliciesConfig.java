package com.azat4dev.booking.searchlistingsms.config.acl.domain;

import com.azat4dev.booking.shared.config.domain.AutoConnectPoliciesToBus;
import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@AutoConnectPoliciesToBus
@AllArgsConstructor
@Configuration
public class PoliciesConfig {

    private final MessageBus<String> messageBus;

    @PostConstruct
    public void init() {
    }
}
