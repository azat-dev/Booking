import {getChannelServiceId} from "./utils";
import {AvroModelsGenerator} from "./AvroModelsGenerator";
import {ConverterJsonSchemaToAvro} from "./ConverterJsonSchemaToAvro";
import {getInputChannelForOperation, getInputTypeInterfaceForEndpoint} from "./endpoints/utils";

export class AvroDtoGenerator {

    constructor(getPackageForService) {
        this._modelsGenerator = new AvroModelsGenerator();
        this._getPackageForService = getPackageForService;
    }

    _getChannelMessageSchema = (namespace, payloads) => {

        return {
            namespace,
            name: "ChannelMessageDTO",
            type: "record",
            fields: [
                {
                    name: "id",
                    type: "string"
                },
                {
                    name: "type",
                    type: "string"
                },
                {
                    name: "correlationId",
                    type: [
                        "string",
                        "null"
                    ]
                },
                {
                    name: "replyToId",
                    type: [
                        "string",
                        "null"
                    ]
                },
                {
                    name: "payload",
                    type: payloads
                }
            ]
        };
    }

    _getDtoForMessages = async (messages, packageName, serviceId, channelId, modelsSuffix, getEndpointInterfaces) => {

        const result = [];
        const pckg = `${packageName}.dto.${this._getPackageForService(serviceId)}.${channelId.toLowerCase().replace(/\//g, '.')}`;
        const filePackage = `${this._getPackageForService(serviceId)}.${channelId.toLowerCase().replace(/\//g, '.')}`;

        const messageSchema = this._getChannelMessageSchema(
            pckg,
            messages.map(message => {
                return {"name": message.id() + modelsSuffix, type: `${pckg}.${message.id()}${modelsSuffix}`}
            })
        );

        result.push({
            fileName: `ChannelMessage${modelsSuffix}.avsc`,
            packageName: filePackage,
            content: JSON.stringify(messageSchema, null, 2)
        });

        for (let messageIndex = 0; messageIndex < messages.length; messageIndex++) {

            const message = messages[messageIndex];
            const messageId = message.id();
            const payload = message.payload();

            const sch = payload.json();
            const converter = new ConverterJsonSchemaToAvro(
                modelsSuffix,
                pckg,
                packageName + '.dto.Undefined',
            );

            const avroSchemas = converter.convert(sch, getEndpointInterfaces(channelId, messageId));
            avroSchemas.forEach(avroSchema => {
                result.push({
                    fileName: `${avroSchema.name}.avsc`,
                    packageName: filePackage,
                    content: JSON.stringify(avroSchema, null, 2)
                });
            });

        }

        return result;
    }

    _createGetEndpointInterfaces = (operations, modelsSuffix) => {

        return (channelId, messageId) => {

            const interfaces = operations.map(operation => {

                const messageIds = operation.messages().all().map(i => i.id());
                if (!messageIds.includes(messageId)) {
                    return null;
                }

                const operationId = operation.id();
                if (messageIds.length === 1) {
                    return null;
                }

                const inputChannel = getInputChannelForOperation(operation);
                if (inputChannel.id() !== channelId) {
                    return null;
                }

                return getInputTypeInterfaceForEndpoint(operationId, modelsSuffix);
            }).filter(i => !!i);

            if (interfaces.length === 0) {
                return;
            }

            debugger
            return interfaces;
        }
    }

    _generateForChannels = async (asyncapi, modelsSuffix, packageName) => {

        const result = [];

        const channels = asyncapi.channels().all();

        const getEndpointInterfaces = this._createGetEndpointInterfaces(asyncapi.operations().filterByReceive(), modelsSuffix);

        for (let channelIndex = 0; channelIndex < channels.length; channelIndex++) {

            const channel = channels[channelIndex];

            const serviceId = getChannelServiceId(channel);
            const channelId = channel.id();
            if (!serviceId) {
                console.log(`There is no 'x-service' for channel: ${channelId}`);
                throw new Error(`There is no 'x-service' for channel: ${channelId}`);
            }

            const messages = channel.messages().all();

            result.push(...await this._getDtoForMessages(messages, packageName, serviceId, channelId, modelsSuffix, getEndpointInterfaces));
        }

        return result;
    };

    _generateForExternalEndpoints = async (asyncapi, modelsSuffix, packageName) => {

        const result = [];

        const operations = asyncapi.operations().filterBySend();
        const getEndpointInterfaces = this._createGetEndpointInterfaces(asyncapi.operations().filterByReceive(), modelsSuffix);

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
            result.push(...await this._getDtoForMessages(messages, packageName, serviceId, channelId, modelsSuffix, getEndpointInterfaces));
        }

        return result;
    };

    generate = async (asyncapi, modelsSuffix, packageName) => {
        return [
            ...(await this._generateForExternalEndpoints(asyncapi, modelsSuffix, packageName)),
            ...(await this._generateForChannels(asyncapi, modelsSuffix, packageName)),
            {
                fileName: `Undefined.avsc`,
                packageName: 'dto',
                content: JSON.stringify({
                    namespace: packageName + '.dto',
                    name: 'Undefined',
                    type: 'record',
                    fields: [
                    ]
                }, null, 2)
            }
        ]
    }
}