import {AsyncAPIInputProcessor, JavaGenerator, JsonSchemaInputProcessor} from "@asyncapi/modelina";
import {getChannelServiceId} from "./utils";

export class AvroModelsGenerator {

    _convertToCommonModel = (payload) => {
        const schema = AsyncAPIInputProcessor.convertToInternalSchema(payload);
        const newCommonModel =
            JsonSchemaInputProcessor.convertSchemaToCommonModel(schema);

        newCommonModel.$id = payload.meta().id;
        return newCommonModel;
    };

    _generateModels = async (input, options, packagesForServices) => {

        return {};
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

    _getContent = (packageName) => {
        return {
            "namespace": packageName,
            "name": "MessageDTO",
            "type": "record",
            "fields": [
            {
                "name": "id",
                "type": "string"
            },
            {
                "name": "type",
                "type": "string"
            },
            {
                "name": "correlationId",
                "type": [
                    "string",
                    "null"
                ]
            },
            {
                "name": "replyToId",
                "type": [
                    "string",
                    "null"
                ]
            },
            {
                "name": "payload",
                "type": [
                    {
                        "type": "record",
                        "name": "JoinedMessageDTO",
                        "fields": [
                            {
                                "name": "messageId1",
                                "type": "string"
                            },
                            {
                                "name": "messageId2",
                                "type": "string"
                            }
                        ]
                    },
                    {
                        "type": "record",
                        "name": "TestMessageDTO",
                        "fields": [
                            {
                                "name": "id",
                                "type": "string"
                            },
                            {
                                "name": "payload",
                                "type": "string"
                            }
                        ]
                    }
                ]
            }
        ]
        }
    }
}