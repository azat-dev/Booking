import {AsyncAPIInputProcessor, JavaGenerator, JsonSchemaInputProcessor} from "@asyncapi/modelina";
import {getChannelServiceId} from "./utils";

export class ModelsGenerator {

    constructor(javaGenerator) {
        this._javaGenerator = javaGenerator;
    }

    _convertToCommonModel = (payload) => {
        const schema = AsyncAPIInputProcessor.convertToInternalSchema(payload);
        const newCommonModel =
            JsonSchemaInputProcessor.convertSchemaToCommonModel(schema);

        newCommonModel.$id = payload.meta().id;
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

    _generateModels = async (input, options, packagesForServices) => {

        return this._javaGenerator.generateCompleteModels(
            input,
            {packageName: options.packageName + '.dto.' + packagesForServices[options.serviceId]}
        )
    }

    generate = async (asyncapi, modelsSuffix, packagesForServices) => {

        const result = [];

        const channels = asyncapi.channels().all();

        for (let i = 0; i < channels.length; i++) {

            const channel = channels[i];

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

                const models = await this._generateModels(
                    commonModel,
                    {modelsSuffix, channelId, serviceId, id: message.id()},
                    packagesForServices
                );

                models.forEach(item => {
                    result.push(
                        {
                            fileName: `${item.modelName}.java`,
                            packageName: item.packageName,
                            content: item.result
                        }
                    )
                })
            }
        }

        return result;
    };
}