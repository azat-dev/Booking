package com.azat4dev.booking.searchlistingsms.config.common.infrastructure;

import com.azat4dev.booking.shared.config.infrastracture.serializers.DefaultTimeSerializerConfig;
import com.azat4dev.booking.shared.config.infrastracture.services.DefaultTimeProviderConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@AllArgsConstructor
@Import({DefaultTimeProviderConfig.class, DefaultTimeSerializerConfig.class})
@Configuration
public class CommonBeansConfig {
}
