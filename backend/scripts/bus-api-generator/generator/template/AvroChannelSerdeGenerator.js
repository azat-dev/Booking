import {
    capitalize,
    convertChannelIdToConstantName,
    convertChannelIdToPackageName,
    getChannelServiceId,
    removeSlashes
} from "./utils";
import {AvroModelsGenerator} from "./AvroModelsGenerator";

export class AvroChannelSerdeGenerator {

    constructor(getPackageForService) {
        this._modelsGenerator = new AvroModelsGenerator();
        this._getPackageForService = getPackageForService;
    }

    _getContentForSerializer = (packageName, className, channelId, imports) => {

        return `
package ${packageName};

import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageSerializer;
import com.azat4dev.booking.shared.infrastructure.bus.Message;

${imports.map(i => `import ${i};`).join('\n')}
import java.io.IOException;

import io.micrometer.observation.annotation.Observed;


@Observed
public class ${className}Serializer implements MessageSerializer {

    @Override
    public byte[] serialize(Message message) throws Exception.FailedSerialize {
        try {

            final var dto = new ChannelMessageDTO(
                message.id(),
                message.type(),
                message.correlationId().orElse(null),
                message.replyTo().orElse(null),
                message.payload()
            );
            return ChannelMessageDTO.getEncoder().encode(dto).array();
        } catch (IOException e) {
            throw new MessageSerializer.Exception.FailedSerialize(e);
        }
    }
}
        `.trim();
    }

    _getContentForDeserializer = (packageName, className, channelId, imports) => {

        return `
package ${packageName};

import com.azat4dev.booking.shared.infrastructure.bus.serialization.MessageDeserializer;
import com.azat4dev.booking.shared.infrastructure.bus.Message;

${imports.map(i => `import ${i};`).join('\n')}
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import io.micrometer.observation.annotation.Observed;


@Observed
public class ${className}Deserializer implements MessageDeserializer {

    @Override
    public Message deserialize(byte[] serializedMessage) throws Exception.FailedDeserialize {
        try {
            final var dto = ChannelMessageDTO.getDecoder().decode(serializedMessage);

            return new Message(
                dto.getId().toString(),
                dto.getType().toString(),
                LocalDateTime.now(),
                Optional.ofNullable(dto.getCorrelationId()).map(CharSequence::toString),
                Optional.ofNullable(dto.getReplyToId()).map(CharSequence::toString),
                dto.getPayload()
            );
        } catch (IOException e) {
            throw new MessageDeserializer.Exception.FailedDeserialize(e);
        }
    }
}
        `.trim();
    }

    _getContentForSerializersConfig = (packageName, imports, className, serializers) => {
        return `
package ${packageName};

import com.azat4dev.booking.shared.infrastructure.bus.serialization.NewSerializerForChannels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

${imports.map(i => `import ${i};`).join('\n')}

import java.util.List;

@Configuration
public class ${className} {

    ${
            serializers.map(serializer => {
                return `
    @Bean
    public NewSerializerForChannels ${serializer.methodName}Serializer() {
        return new NewSerializerForChannels(
            List.of(Channels.${serializer.channelConstant}.getValue()),
            new ${serializer.className}Serializer()
        );
    }
    `.trim();
            }).join('\n')
        }
}

`.trim();
    }

    _getContentForDeserializersConfig = (packageName, imports, className, serializers) => {
        return `
package ${packageName};

import com.azat4dev.booking.shared.infrastructure.bus.serialization.NewDeserializerForChannels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

${imports.map(i => `import ${i};`).join('\n')}

import java.util.List;

@Configuration
public class ${className} {

    ${
            serializers.map(serializer => {
                return `
    @Bean
    public NewDeserializerForChannels ${serializer.methodName}Deserializer() {
        return new NewDeserializerForChannels(
            List.of(Channels.${serializer.channelConstant}.getValue()),
            new ${serializer.className}Deserializer()
        );
    }
    `.trim();
            }).join('\n')
        }
}

`.trim();
    }

    generate = async (asyncapi, modelsSuffix, packageName) => {

        const result = [];

        const channels = asyncapi.channels().all();
        const serializersByService = {};

        for (let channelIndex = 0; channelIndex < channels.length; channelIndex++) {

            const channel = channels[channelIndex];
            const channelId = channel.id();
            const serviceId = getChannelServiceId(channel);
            const servicePackage = this._getPackageForService(serviceId);
            const className = capitalize(servicePackage) + capitalize(removeSlashes(channelId));
            const channelPackage = convertChannelIdToPackageName(channelId);

            result.push({
                fileName: `${className}Serializer.java`,
                packageName: `${packageName}.serialization.${servicePackage}`,
                content: this._getContentForSerializer(
                    `${packageName}.serialization.${servicePackage}`,
                    className,
                    convertChannelIdToConstantName(channelId),
                    [
                        `${packageName}.dto.${servicePackage}.${channelPackage}.ChannelMessageDTO`
                    ]
                )
            });

            result.push({
                fileName: `${className}Deserializer.java`,
                packageName: `${packageName}.serialization.${servicePackage}`,
                content: this._getContentForDeserializer(
                    `${packageName}.serialization.${servicePackage}`,
                    className,
                    convertChannelIdToConstantName(channelId),
                    [
                        `${packageName}.dto.${servicePackage}.${channelPackage}.ChannelMessageDTO`
                    ]
                )
            });

            serializersByService[serviceId] = serializersByService[serviceId] || [];
            serializersByService[serviceId].push(
                {
                    packageName,
                    className,
                    channelId,
                    channelConstant: convertChannelIdToConstantName(channelId),
                    methodName: servicePackage + capitalize(removeSlashes(channelId))
                }
            );
        }

        Object.getOwnPropertyNames(serializersByService).forEach(serviceId => {
            const servicePackage = this._getPackageForService(serviceId);

            const serializers = serializersByService[serviceId];

            result.push({
                fileName: `${capitalize(servicePackage)}SerializersConfig.java`,
                packageName: `${packageName}.serialization.config`,
                content: this._getContentForSerializersConfig(
                    `${packageName}.serialization.config`,
                    [
                        ...serializers.map(s => `${packageName}.serialization.${servicePackage}.${s.className}Serializer`),
                        `${packageName}.Channels`
                    ],
                    capitalize(this._getPackageForService(serviceId)) + "SerializersConfig",
                    serializers
                )
            });

            result.push({
                fileName: `${capitalize(servicePackage)}DeserializersConfig.java`,
                packageName: `${packageName}.serialization.config`,
                content: this._getContentForDeserializersConfig(
                    `${packageName}.serialization.config`,
                    [
                        ...serializers.map(s => `${packageName}.serialization.${servicePackage}.${s.className}Deserializer`),
                        `${packageName}.Channels`
                    ],
                    capitalize(this._getPackageForService(serviceId)) + "DeserializersConfig",
                    serializers
                )
            });
        });

        return result;
    };
}