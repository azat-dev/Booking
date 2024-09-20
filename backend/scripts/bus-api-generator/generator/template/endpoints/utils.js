import {capitalize, getChannelServiceId, removeSlashes} from "../utils";

export const getInputChannelForOperation = (operation) => {
    const inputChannels = operation.channels().all();
    if (inputChannels.length !== 1) {
        console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
        throw new Error("Operation doesn't have exactly one channel");
    }

    return inputChannels[0];
}

export const getInputTypes = (operation, modelsSuffix, getPackageForService) => {

    const inputChannel = getInputChannelForOperation(operation);
    const serviceId = getChannelServiceId(inputChannel);

    return operation.messages().all().map(m => {
        return {
            name: m.id(),
            className: m.id() + modelsSuffix,
            customService: m.payload().json()['x-service'],
            packageName: getPackageForService(serviceId)
        }
    });
}

export const getInputTypeInterfaceForEndpoint = (operationId, modelsSuffix) => {
    return `${capitalize(removeSlashes(operationId))}Input${modelsSuffix}`;
}

export const getInputTypeNameForEndpoint = (operationId, inputTypes, modelsSuffix, channel, getServicePackage) => {
    if (inputTypes.length === 1) {
        return {
            className: inputTypes[0].className,
            packageName: inputTypes[0].packageName
        };
    }

    return {
        isInterface: true,
        className: getInputTypeInterfaceForEndpoint(operationId, modelsSuffix),
        packageName:  `${getServicePackage(getChannelServiceId(channel))}.${channelIdToPackageName(channel.id())}`
    };
}

export const getInputChannel = (operation) => {

    const inputChannels = operation.channels?.();

    if (inputChannels.length !== 1) {
        console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
        throw new Error("Operation doesn't have exactly one channel");
    }

    return inputChannels[0];
}

export const channelIdToPackageName = (channelId) => {
    return channelId.toLowerCase().replace(/\//g, '.');
}