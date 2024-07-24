package com.azat4dev.booking.shared.infrastructure.serializers;

import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEvent;
import com.azat4dev.booking.shared.data.serializers.MapAnyDomainEventImpl;
import com.azat4dev.booking.shared.data.serializers.MapDomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MapAnyDomainEventImplTests {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TestDomainEvent implements DomainEventPayload {
        @JsonProperty("value")
        private String value;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TestDomainEventDTO {
        @JsonProperty("value")
        private String value;
    }

    private static class MapTestDomainEvent implements MapDomainEvent<TestDomainEvent, TestDomainEventDTO> {

        @Override
        public TestDomainEventDTO serialize(TestDomainEvent dm) {
            return new TestDomainEventDTO(dm.getValue());
        }


        @Override
        public TestDomainEvent deserialize(TestDomainEventDTO dto) {
            return new TestDomainEvent(dto.getValue());
        }

        @Override
        public Class getOriginalClass() {
            return TestDomainEvent.class;
        }

        @Override
        public Class getSerializedClass() {
            return TestDomainEventDTO.class;
        }
    }

    MapAnyDomainEvent createSUT() {
        final var objectMapper = new ObjectMapper();
        // Register the Jdk8Module to handle Optional
        objectMapper.registerModule(new Jdk8Module());

        return new MapAnyDomainEventImpl(
            List.of(new MapTestDomainEvent())
        );
    }

    @Test
    void test_serialize() {

        // Given
        final var sut = createSUT();
        final var event = new TestDomainEvent(
            "testvalue"
        );

        // When
        final var serialized = sut.toDTO(event);
        final var deserializedValue = sut.fromDTO(serialized);

        // Then
        assertThat(serialized).isNotNull();
        assertThat(deserializedValue).isEqualTo(event);
    }
}
