import {File} from '@asyncapi/generator-react-sdk'

const messageFileContent = (packageName) => `
package ${packageName};

import java.time.LocalDateTime;

public record Message<T>(
    String id,
    String type,
    LocalDateTime timestamp,
    T payload
) {
}
`.trim();

const endpointFileContent = (
    packageName,
    interfaceName,
    inputChannelConstantName,
    replyChannelConstantName,
    inputMessageType,
    inputMessageClass,
    returnTypes,
    dtoPackage
) => `
package ${packageName};

import com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint;
import ${dtoPackage}.${inputMessageClass};
${returnTypes.map(i => `import ${dtoPackage}.` + i.className + ';').join("\n")}

import java.util.Optional;

public interface ${interfaceName} extends BusApiEndpoint<${inputMessageClass}> {

    @Override
    default String getInputAddress() {
        return Channels.${inputChannelConstantName}.getValue();
    }
    
    @Override
    default Optional<String> getReplyAddress() {
        ${replyChannelConstantName ? `return Optional.of(Channels.${replyChannelConstantName}.getValue());` : 'return Optional.empty();'}
    }
    
    @Override
    default MessageInfo getInputMessageInfo() {
        return new MessageInfo("${inputMessageType}", ${inputMessageClass}.class);
    }
    
    @Override
    default MessageInfo[] getResponseMessagesInfo() {
        return new MessageInfo[] { 
            ${returnTypes.map(i => `new MessageInfo("${i.type}", ${i.className}.class)`).join(", ")} 
        };
    }
}
`.trim();

const camelCaseToScreamingSnakeCase = (str) => {
    return str.replace(/([A-Z])/g, '_$1').toUpperCase();
}

const channelConstantFileContent = (packageName, items) => `
package ${packageName};

public enum Channels {
    ${items.map(item => `${item.id}("${item.value}"),`).join("\n")};

    private final String value;

    Channels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
`.trim();

const capitalize = (str) => str.charAt(0).toUpperCase() + str.slice(1);

const convertChannelIdToConstantName = (id) => {
    return camelCaseToScreamingSnakeCase(id.replaceAll('/', '_'));
}

const getChannelConstantNames = (asyncapi) => {
    const result = [];

    asyncapi.channels()
        .forEach(channel => {
            result.push(
                {
                    id: convertChannelIdToConstantName(channel.id()),
                    value: channel.address()
                }
            );
        });

    return result;
}


export default function (options) {
    const {asyncapi} = options;

    const dtoPackage = options.params.dtoPackage;
    const packageName = options.params.package;

    const channelConstantNames = getChannelConstantNames(asyncapi);
    return (
        [
            <File name={"Message.java"}>
                {messageFileContent(packageName)}
            </File>,
            <File name={"Channels.java"}>
                {channelConstantFileContent(packageName, channelConstantNames)}
            </File>,
            ...asyncapi.operations().filterByReceive()
                .map(operation => {

                    const className = capitalize(operation.id()) + "Endpoint";
                    const inputChannelConstantName = convertChannelIdToConstantName(operation.channels()[0].id());
                    const replyChannelConstantName = convertChannelIdToConstantName(operation.reply().channel().id());

                    const returnType = operation.reply().messages().collections.map(m => ({
                        type: m.payload().title(),
                        className: m.payload().title() + 'DTO'
                    }));
                    const inputType = operation.messages()[0].payload().title();
                    const inputTypeDto = inputType + "DTO";
                    return (
                        <File name={`${className}.java`}>
                            {endpointFileContent(packageName, className, inputChannelConstantName, replyChannelConstantName, inputType, inputTypeDto, returnType, dtoPackage)}
                        </File>
                    )
                })
        ]
    )
}