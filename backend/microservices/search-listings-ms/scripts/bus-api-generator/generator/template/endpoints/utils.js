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
            packageName: getPackageForService(serviceId)
        }
    });
}

export const getInputTypeNameForEndpoint = (operationId, inputTypes, modelsSuffix) => {
    if (inputTypes.length === 1) {
        return inputTypes[0].className;
    }

    return `${capitalize(removeSlashes(operationId))}Input${modelsSuffix}`;
}

export const getInputChannel = (operation) => {

    const inputChannels = operation.channels?.();

    if (inputChannels.length !== 1) {
        console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
        throw new Error("Operation doesn't have exactly one channel");
    }

    return inputChannels[0];
}