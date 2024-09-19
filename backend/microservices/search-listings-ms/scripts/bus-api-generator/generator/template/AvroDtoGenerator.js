import {capitalize, getChannelServiceId} from "./utils";
import {AvroModelsGenerator} from "./AvroModelsGenerator";
import {ConverterJsonSchemaToAvro} from "./ConverterJsonSchemaToAvro";
import {
    channelIdToPackageName,
    getInputChannelForOperation,
    getInputTypeNameForEndpoint,
    getInputTypes
} from "./endpoints/utils";

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

    _deletePrefix = (str, prefix) => {
        return str.startsWith(prefix) ? str.substring(prefix.length) : str;
    }

    _getDtoForMessages = async (messages, packageName, serviceId, channelId, modelsSuffix, getEndpointInterfaces, basePackageName) => {

        const result = [];
        const channelPackage = channelIdToPackageName(channelId);
        const channelNamespace = `${packageName}.dto.${this._getPackageForService(serviceId)}.${channelPackage}`;
        const filePackage = `${this._getPackageForService(serviceId)}.${channelPackage}`;

        const payloads = [];

        for (let messageIndex = 0; messageIndex < messages.length; messageIndex++) {

            const message = messages[messageIndex];
            const messageId = message.id();
            const payload = message.payload();

            const schema = payload.json();
            const converter = new ConverterJsonSchemaToAvro(
                modelsSuffix,
                channelNamespace,
                packageName + '.dto.Undefined',
                basePackageName + '.dto',
                this._getPackageForService
            );

            const inputInterfaces = getEndpointInterfaces(channelId, messageId);

            const messageConversionResult = converter.convert(schema);
            messageConversionResult.schema["_interfaces"] = inputInterfaces.map(i => i.className);
            messageConversionResult.schema["_interfacesImports"] = inputInterfaces.map(i => `import ${basePackageName}.dto.${i.packageName}.${i.className};`);

            [messageConversionResult.schema, ...messageConversionResult.otherTypes].forEach(avroSchema => {
                let filePackageName = filePackage;
                if (avroSchema.namespace !== channelNamespace) {
                    filePackageName = `${this._deletePrefix(avroSchema.namespace, basePackageName + '.')}`;
                }

                result.push({
                    fileName: `${avroSchema.name}.avsc`,
                    packageName: filePackageName,
                    content: JSON.stringify(avroSchema, null, 2)
                });
            });

            payloads.push({
                name: capitalize(schema.title).trim(),
                type: `${messageConversionResult.schema.namespace}.${messageConversionResult.schema.name}`
            });

        }

        const messageSchema = this._getChannelMessageSchema(
            channelNamespace,
            payloads
        );

        result.push({
            fileName: `ChannelMessage${modelsSuffix}.avsc`,
            packageName: filePackage,
            content: JSON.stringify(messageSchema, null, 2)
        });

        return result;
    }

    _createGetEndpointInterfaces = (operations, modelsSuffix) => {

        return (channelId, messageId) => {

            const interfaces = operations.map(operation => {

                const messageIds = operation.messages().all().map(i => i.id());
                if (!messageIds.includes(messageId)) {
                    return null;
                }

                if (messageIds.length === 1) {
                    return null;
                }

                const inputChannel = getInputChannelForOperation(operation);
                if (inputChannel.id() !== channelId) {
                    return null;
                }

                const inputTypeData = getInputTypeNameForEndpoint(
                    operation.id(),
                    getInputTypes(operation, modelsSuffix, this._getPackageForService),
                    modelsSuffix,
                    inputChannel,
                    this._getPackageForService
                );

                if (!inputTypeData.isInterface) {
                    return null;
                }

                return inputTypeData;
            }).filter(i => !!i);

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

            result.push(
                ...await this._getDtoForMessages(
                    messages,
                    packageName,
                    serviceId,
                    channelId,
                    modelsSuffix,
                    getEndpointInterfaces,
                    packageName
                )
            );
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
            result.push(...await this._getDtoForMessages(messages, packageName, serviceId, channelId, modelsSuffix, getEndpointInterfaces, packageName));
        }

        return result;
    };

    generate = async (asyncapi, modelsSuffix, packageName) => {
        return [
            ...(await this._generateForExternalEndpoints(asyncapi, modelsSuffix, packageName)),
            ...(await this._generateForChannels(asyncapi, modelsSuffix, packageName)),
            {
                fileName: `Undefined.avsc`,
                packageName: '',
                content: JSON.stringify({
                    namespace: packageName + '.dto',
                    name: 'Undefined',
                    type: 'record',
                    fields: []
                }, null, 2)
            }
        ]
    }
}