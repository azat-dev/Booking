import {capitalize, convertChannelIdToConstantName, getChannelServiceId, removeSlashes} from "./utils";

export  class EndpointFileGenerator {

    generate = (operation, options, packagesByServices) => {

        if (!operation.id()) {
            console.error("Operation doesn't have id", JSON.stringify(operation, null, 2));
            throw new Error("Operation doesn't have id");
        }

        const operationId = operation.id();

        const className = capitalize(removeSlashes(operationId)) + "Endpoint";
        const inputChannels = operation.channels().all();
        if (inputChannels.length !== 1) {
            console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
            throw new Error("Operation doesn't have exactly one channel");
        }

        const inputChannel = inputChannels[0];
        const serviceId = inputChannel.extensions().all().find(i => i.id() === 'x-service')?.value();
        const packageName = options.packageName + '.endpoints';

        const inputChannelId = inputChannel?.id?.();
        const inputChannelConstantName = inputChannelId && convertChannelIdToConstantName(inputChannelId);

        const reply = operation.reply && operation.reply();


        const replyChannel = reply.channel?.();
        const replyChannelId = reply && reply.channel?.()?.id?.();

        const replyChannelConstantName = replyChannelId && convertChannelIdToConstantName(replyChannelId);
        const replyLocation = reply && reply.address?.()?.location?.();
        const hasDynamicReplyAddress = replyLocation === '$message.header#/x-reply-to';

        const returnType = (reply?.messages?.()?.all() ?? []).map(m => ({
            type: m.meta().id,
            className: m.meta().id + options.modelsSuffix,
            packageName: packagesByServices[getChannelServiceId(replyChannel)]
        }));

        const inputTypes = operation.messages().all().map(m => {
            return {
                name: m.id(),
                className: m.id() + options.modelsSuffix,
                packageName: packagesByServices[getChannelServiceId(replyChannel)]
            }
        });
        let inputTypeDto = inputTypes[0].className;
        if (inputTypes.length > 1) {
            inputTypeDto = capitalize(removeSlashes(inputChannelId)) + options.modelsSuffix;
        }

        return (
            {
                fileName: `${className}.java`,
                packageName: packageName,
                content: this._renderContent(
                    options.packageName,
                    [
                        'java.util.Optional',
                        'com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint',
                        `${options.packageName}.Channels`,
                        ...(inputTypes.map(i => `${options.packageName}.dto.${i.packageName}.${i.className}`)),
                        ...(returnType.map(i => `${options.packageName}.dto.${i.packageName}.${i.className}`))
                    ],
                    inputTypes,
                    inputChannelConstantName,
                    hasDynamicReplyAddress,
                    replyChannelConstantName,
                    className,
                    inputTypeDto,
                    returnType
                )
            }
        )
    }

    _renderContent = (
        packageName,
        imports,
        inputMessageTypes,
        inputChannelConstantName,
        hasDynamicReplyAddress,
        replyChannelConstantName,
        interfaceName,
        inputMessageClass,
        returnTypes
    ) => `
package ${packageName}.endpoints;

${imports.map(i => `import ${i};`).join("\n")}

public interface ${interfaceName} extends BusApiEndpoint<${inputMessageClass}> {

    MessageInfo[] INPUT_MESSAGE_TYPES = new MessageInfo[] {
        ${inputMessageTypes.map(i => `new MessageInfo("${i.name}", ${i.className}.class)`).join("\n, ")}
    };
    
    @Override
    default String getInputAddress() {
        return Channels.${inputChannelConstantName}.getValue();
    }

    @Override
    default boolean hasDynamicReplyAddress() {
        return ${hasDynamicReplyAddress ? 'true' : 'false'};
    }
    
    @Override
    default Optional<String> getStaticReplyAddress() {
        ${replyChannelConstantName ? `return Optional.of(Channels.${replyChannelConstantName}.getValue());` : 'return Optional.empty();'}
    }
    
    @Override
    default MessageInfo[] getInputMessageInfo() {
        return INPUT_MESSAGE_TYPES;
    }
    
    @Override
    default MessageInfo[] getResponseMessagesInfo() {
        return new MessageInfo[] { 
            ${returnTypes.map(i => `new MessageInfo("${i.type}", ${i.className}.class)`).join("\n, ")} 
        };
    }
}
`.trim();
}