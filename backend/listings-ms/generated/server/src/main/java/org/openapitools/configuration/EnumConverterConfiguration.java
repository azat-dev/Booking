package org.openapitools.configuration;

import com.azat4dev.booking.listingsms.generated.server.model.ListingStatusDTO;
import com.azat4dev.booking.listingsms.generated.server.model.PropertyTypeDTO;
import com.azat4dev.booking.listingsms.generated.server.model.RoomTypeDTO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EnumConverterConfiguration {

    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.listingStatusDTOConverter")
    Converter<String, ListingStatusDTO> listingStatusDTOConverter() {
        return new Converter<String, ListingStatusDTO>() {
            @Override
            public ListingStatusDTO convert(String source) {
                return ListingStatusDTO.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.propertyTypeDTOConverter")
    Converter<String, PropertyTypeDTO> propertyTypeDTOConverter() {
        return new Converter<String, PropertyTypeDTO>() {
            @Override
            public PropertyTypeDTO convert(String source) {
                return PropertyTypeDTO.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.roomTypeDTOConverter")
    Converter<String, RoomTypeDTO> roomTypeDTOConverter() {
        return new Converter<String, RoomTypeDTO>() {
            @Override
            public RoomTypeDTO convert(String source) {
                return RoomTypeDTO.fromValue(source);
            }
        };
    }

}
