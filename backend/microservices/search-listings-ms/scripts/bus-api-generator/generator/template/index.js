import {FilesWriter} from "./FilesWriter";
import {ChannelsFileGenerator} from "./ChannelsFileGenerator";
import {EndpointFileGenerator} from "./endpoints/EndpointFileGenerator";
import {EndpointFilesGenerator} from "./endpoints/EndpointFilesGenerator";
import {MessageFileGenerator} from "./MessageFileGenerator";
import {EndpointInputInterfaceGenerator} from "./endpoints/EndpointInputInterfaceGenerator";
import {AvroDtoGenerator} from "./AvroDtoGenerator";
import {AvroChannelSerdeGenerator} from "./AvroChannelSerdeGenerator";

const SUFFIX = "DTO";

export default async function (options) {

    const {asyncapi} = options;

    const dtoPackage = options.params.dtoPackage;
    const packageName = options.params.package;
    const outputDir = options.params.outputDir;
    const outputAvroDir = options.params.outputAvroDir;

    const getPackageForService = (serviceId) => {
        const result = options.params.packageNamesByServices[serviceId];

        if (!result) {
            throw new Error(`There is no package for service: ${serviceId}`);
        }

        return result;
    }

    const modelsSuffix = SUFFIX;

    const channelsFileGenerator = new ChannelsFileGenerator();

    const basePackageName = options.params.package;

    const channelSerdeGenerator = new AvroChannelSerdeGenerator(getPackageForService);

    const endpointInputInterfaceGenerator = new EndpointInputInterfaceGenerator(basePackageName, modelsSuffix, getPackageForService);
    const endpointFileGenerator = new EndpointFileGenerator(getPackageForService);
    const endpointFilesGenerator = new EndpointFilesGenerator(
        endpointFileGenerator,
        endpointInputInterfaceGenerator,
        packageName,
        getPackageForService
    );

    const dtoGenerator = new AvroDtoGenerator(getPackageForService);

    const messageFileGenerator = new MessageFileGenerator();

    const generatedModels =
        [
            channelsFileGenerator.generate(packageName, asyncapi.channels().all()),
            messageFileGenerator.generate(packageName),
            ...endpointFilesGenerator.generate(asyncapi, {
                ...options,
                modelsSuffix: SUFFIX,
                dtoPackage,
                packageName
            }),
        ];


    const writer = new FilesWriter();
    await writer.write(generatedModels, outputDir, true);
    await writer.write(
        await dtoGenerator.generate(asyncapi, SUFFIX, packageName),
        outputAvroDir,
        true
    );

    await writer.write(
        await channelSerdeGenerator.generate(asyncapi, SUFFIX, packageName),
        outputDir,
        true
    );
    return [];
}