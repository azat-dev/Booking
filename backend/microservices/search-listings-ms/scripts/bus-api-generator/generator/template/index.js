import {FilesWriter} from "./FilesWriter";
import {ChannelsFileGenerator} from "./ChannelsFileGenerator";
import {EndpointFileGenerator} from "./endpoints/EndpointFileGenerator";
import {EndpointFilesGenerator} from "./endpoints/EndpointFilesGenerator";
import {CustomJavaGenerator} from "./CustomJavaGenerator";
import {ModelFilesGenerator} from "./ModelFilesGenerator";
import {DtoGenerator} from "./DtoGenerator";
import {ModelsGenerator} from "./ModelsGenerator";
import {MessageFileGenerator} from "./MessageFileGenerator";
import {EndpointInputInterfaceGenerator} from "./endpoints/EndpointInputInterfaceGenerator";

const SUFFIX = "DTO";

export default async function (options) {

    const {asyncapi} = options;

    const dtoPackage = options.params.dtoPackage;
    const packageName = options.params.package;
    const outputDir = options.params.outputDir;

    const getPackageForService = (serviceId) => {
        const result = options.params.packageNamesByServices[serviceId];

        if (!result) {
            throw new Error(`There is no package for service: ${serviceId}`);
        }

        return result;
    }

    const modelsSuffix  = SUFFIX;

    const javaGenerator = new CustomJavaGenerator(options.id, modelsSuffix);
    const modelFilesGenerator = new ModelFilesGenerator(getPackageForService);

    const channelsFileGenerator = new ChannelsFileGenerator();

    const basePackageName = options.packageName;
    const endpointInputInterfaceGenerator = new EndpointInputInterfaceGenerator(basePackageName, modelsSuffix, getPackageForService);
    const endpointFileGenerator = new EndpointFileGenerator(getPackageForService);
    const endpointFilesGenerator = new EndpointFilesGenerator(
        endpointFileGenerator,
        endpointInputInterfaceGenerator,
        packageName,
        getPackageForService
    );

    const modelsGenerator = new ModelsGenerator(javaGenerator);
    const dtoGenerator = new DtoGenerator(modelFilesGenerator, getPackageForService);

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
            ...(await dtoGenerator.generate(asyncapi, SUFFIX, packageName))
        ];


    const writer = new FilesWriter();
    await writer.write(generatedModels, outputDir, true);
    return [];
}