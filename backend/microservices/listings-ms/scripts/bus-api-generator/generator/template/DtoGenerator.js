import {getChannelServiceId} from "./utils";
import {AsyncAPIInputProcessor, JsonSchemaInputProcessor} from "@asyncapi/modelina";

export class DtoGenerator {

    constructor(modelsGenerator) {
        this._modelsGenerator = modelsGenerator;
    }

    _convertToCommonModel = (payload) => {
        const schema = AsyncAPIInputProcessor.convertToInternalSchema(payload);
        const newCommonModel =
            JsonSchemaInputProcessor.convertSchemaToCommonModel(schema);

        return newCommonModel;
        // if (newCommonModel.$id !== undefined) {
        //     if (inputModel.models[newCommonModel.$id] !== undefined) {
        //         // Logger.warn(
        //         //     `Overwriting existing model with $id ${newCommonModel.$id}, are there two models with the same id present?`,
        //         //     newCommonModel
        //         // );
        //     }
        //     const metaModel = convertToMetaModel(newCommonModel);
        //     inputModel.models[metaModel.name] = metaModel;
        // } else {
        //     // Logger.warn(
        //     //     'Model did not have $id which is required, ignoring.',
        //     //     newCommonModel
        //     // );
        // }
    };

    generate = async (asyncapi, modelsSuffix, packageName, packagesForServices) => {

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

            for (let messageIndex = 0; messageIndex < messages.length; messageIndex++) {

                const message = messages[messageIndex];
                const commonModel = this._convertToCommonModel(message.payload());

                const models = await this._modelsGenerator.generate(
                    commonModel,
                    {modelsSuffix, channelId, serviceId, id: message.id(), packageName},
                    packagesForServices
                );

                models.forEach(item => {
                    result.push(
                        {
                            fileName: `${item.modelName}.java`,
                            packageName: `${packageName}.dto.${packagesForServices[serviceId]}`,
                            content: item.result
                        }
                    )
                })
            }
        }

        return result;
    };
}