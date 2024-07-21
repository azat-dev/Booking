package data.serializers;

import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.data.serializers.DomainEventsSerializerJson;
import com.azat4dev.booking.shared.data.serializers.MapPayload;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEventSerializerJsonTests {

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

    private static class MapTestDomainEvent implements MapPayload<TestDomainEvent, TestDomainEventDTO> {

        @Override
        public TestDomainEventDTO toDTO(TestDomainEvent dm) {
            return new TestDomainEventDTO(dm.getValue());
        }

        @Override
        public TestDomainEvent toDomain(TestDomainEventDTO dto) {
            return new TestDomainEvent(dto.getValue());
        }

        @Override
        public Class getDomainClass() {
            return TestDomainEvent.class;
        }

        @Override
        public Class getDTOClass() {
            return TestDomainEventDTO.class;
        }
    }

    DomainEventSerializer createSUT() {
        final var objectMapper = new ObjectMapper();
        // Register the Jdk8Module to handle Optional
        objectMapper.registerModule(new Jdk8Module());

        return new DomainEventsSerializerJson(
            objectMapper,
            new MapPayload[]{
                new MapTestDomainEvent(),
            }
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
        final var serialized = sut.serialize(event);
        final var deserializedValue = sut.deserialize(event.getClass(), serialized);

        // Then
        assertThat(serialized).isNotNull();
        assertThat(deserializedValue).isEqualTo(event);
    }
}
