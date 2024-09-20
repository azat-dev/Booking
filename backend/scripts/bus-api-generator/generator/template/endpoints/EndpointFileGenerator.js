import {capitalize, convertChannelIdToConstantName, getChannelServiceId, removeSlashes} from "../utils";
import {
    channelIdToPackageName,
    getInputTypeInterfaceForEndpoint,
    getInputTypeNameForEndpoint,
    getInputTypes
} from "./utils";

export class EndpointFileGenerator {

    constructor(getPackageForService) {
        this._getPackageForService = getPackageForService;
    }

    generate = (operation, options) => {

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
        const packageName = options.packageName + '.endpoints';

        const inputServiceId  = getChannelServiceId(inputChannel);

        const inputChannelId = inputChannel?.id?.();
        const inputChannelConstantName = inputChannelId && convertChannelIdToConstantName(inputChannelId);

        const reply = operation.reply && operation.reply();

        const replyChannel = reply?.channel?.();
        const replyChannelId = reply && reply.channel?.()?.id?.();

        const replyChannelConstantName = replyChannelId && convertChannelIdToConstantName(replyChannelId);
        const replyLocation = reply && reply.address?.()?.location?.();
        const hasDynamicReplyAddress = replyLocation === '$message.header#/x-reply-to';

        const replyMessages = !replyChannel ? [] :  ((reply?.messages?.()?.all()) ?? []);
        const returnType = replyMessages.map(m => ({
            type: m.meta().id,
            className: m.meta().id + options.modelsSuffix,
            packageName: this._getPackageForService(getChannelServiceId(replyChannel)) + '.' + "REPLAY"
        }));

        const inputTypes = getInputTypes(operation, options.modelsSuffix, this._getPackageForService);
        const inputTypeDtoData = getInputTypeNameForEndpoint(operationId, inputTypes, options.modelsSuffix, inputChannel, this._getPackageForService);

        const {className: inputTypeDto, packageName: inputTypeDtoPackage, isInterface} = inputTypeDtoData;

        const inputInterfaceImport = isInterface ? `${options.packageName}.dto.${inputTypeDtoPackage}.${inputTypeDto}` : null;
        const importsForInputTypes = (inputTypes.map(i => {
            if (i.customService) {
                return `${options.packageName}.dto.${this._getPackageForService(i.customService)}.${i.className}`;
            }
            return `${options.packageName}.dto.${i.packageName}.${channelIdToPackageName(inputChannelId)}.${i.className}`;
        }));

        return (
            {
                fileName: `${className}.java`,
                packageName: packageName,
                content: this._renderContent(
                    options.packageName,
                    [
                        'java.util.Optional',
                        'com.azat4dev.booking.shared.infrastructure.api.bus.BusApiEndpoint',
                        inputInterfaceImport,
                        `${options.packageName}.Channels`,
                        ...importsForInputTypes,
                        ...(returnType.map(i => `${options.packageName}.dto.${i.packageName}.${i.className}`))
                    ].filter(i => !!i),
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
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface ${interfaceName} extends BusApiEndpoint<${inputMessageClass}> {

    MessageInfo[] INPUT_MESSAGE_TYPES = new MessageInfo[] {
        ${inputMessageTypes.map(i => `new MessageInfo("${i.name}", ${i.className}.class)`).join("\n, ")}
    };
    
    Set<String> ALLOWED_MESSAGE_TYPES = Arrays.stream(INPUT_MESSAGE_TYPES)
        .map(MessageInfo::messageType).collect(Collectors.toSet());
    
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
    
    @Override
    default boolean isMessageTypeAllowed(String messageType) {
        return ALLOWED_MESSAGE_TYPES.contains(messageType);
    }
}
`.trim();
}