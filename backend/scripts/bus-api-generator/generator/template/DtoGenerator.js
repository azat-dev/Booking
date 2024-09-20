import {getChannelServiceId} from "./utils";
import {AsyncAPIInputProcessor, JsonSchemaInputProcessor} from "@asyncapi/modelina";

export class DtoGenerator {

    constructor(modelsGenerator, getPackageForService) {
        this._modelsGenerator = modelsGenerator;
        this._getPackageForService = getPackageForService;
    }

    _convertToCommonModel = (payload) => {
        const schema = AsyncAPIInputProcessor.convertToInternalSchema(payload);
        const newCommonModel =
            JsonSchemaInputProcessor.convertSchemaToCommonModel(schema);

        return newCommonModel;
    };

    _getDtoForMessages = async (messages, packageName, serviceId, channelId, modelsSuffix) => {

        const result = [];
        for (let messageIndex = 0; messageIndex < messages.length; messageIndex++) {

            const message = messages[messageIndex];
            const commonModel = this._convertToCommonModel(message.payload());

            const models = await this._modelsGenerator.generate(
                commonModel,
                {modelsSuffix, channelId, serviceId, id: message.id(), packageName}
            );

            models.forEach(item => {
                result.push(
                    {
                        fileName: `${item.modelName}.java`,
                        packageName: `${packageName}.dto.${this._getPackageForService(serviceId)}`,
                        content: item.result
                    }
                )
            })
        }

        return result;
    }

    _generateForChannels = async (asyncapi, modelsSuffix, packageName) => {

        const result = [];

        const channels = asyncapi.channels().all();

        for (let channelIndex = 0; channelIndex < channels.length; channelIndex++) {

            const channel = channels[channelIndex];

            const serviceId = getChannelServiceId(channel);
            const channelId = channel.id();
            if (!serviceId) {
                console.log(`There is no 'x-service' for channel: ${channelId}`);
                throw new Error(`There is no 'x-service' for channel: ${channelId}`);
            }

            const messages = channel.messages().all();

            result.push(...await this._getDtoForMessages(messages, packageName, serviceId, channelId, modelsSuffix));
        }

        return result;
    };

    _generateForExternalEndpoints = async (asyncapi, modelsSuffix, packageName) => {

        const result = [];

        const operations = asyncapi.operations().filterBySend();

        for (let operationIndex = 0; operationIndex < operations.length; operationIndex++) {

            const operation = operations[operationIndex];
            const inputChannels = operation.channels?.();

            if (inputChannels.length !== 1) {
                console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
                throw new Error("Operation doesn't have exactly one channel");
            }

            const inputChannel = inputChannels[0];

            const serviceId = getChannelServiceId(inputChannel);
            const channelId = inputChannel.id();

            if (!serviceId) {
                console.log(`There is no 'x-service' for channel: ${channelId}`);
                throw new Error(`There is no 'x-service' for channel: ${channelId}`);
            }

            const messages = inputChannel.messages().all();
            result.push(...await this._getDtoForMessages(messages, packageName, serviceId, channelId, modelsSuffix));
        }

        return result;
    };

    generate = async (asyncapi, modelsSuffix, packageName) => {
        return [
            ...(await this._generateForExternalEndpoints(asyncapi, modelsSuffix, packageName)),
            ...(await this._generateForChannels(asyncapi, modelsSuffix, packageName))
        ]
    }
}