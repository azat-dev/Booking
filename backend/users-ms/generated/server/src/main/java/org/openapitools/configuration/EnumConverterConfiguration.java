package org.openapitools.configuration;

import com.azat4dev.booking.usersms.generated.server.model.EmailVerificationStatusDTO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EnumConverterConfiguration {

    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.emailVerificationStatusDTOConverter")
    Converter<String, EmailVerificationStatusDTO> emailVerificationStatusDTOConverter() {
        return new Converter<String, EmailVerificationStatusDTO>() {
            @Override
            public EmailVerificationStatusDTO convert(String source) {
                return EmailVerificationStatusDTO.fromValue(source);
            }
        };
    }

}
