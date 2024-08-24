import {FilesWriter} from "./FilesWriter";
import {ChannelsFileGenerator} from "./ChannelsFileGenerator";
import {EndpointFileGenerator} from "./EndpointFileGenerator";
import {EndpointFilesGenerator} from "./EndpointFilesGenerator";
import {CustomJavaGenerator} from "./CustomJavaGenerator";
import {ModelFilesGenerator} from "./ModelFilesGenerator";
import {DtoGenerator} from "./DtoGenerator";
import {ModelsGenerator} from "./ModelsGenerator";
import {MessageFileGenerator} from "./MessageFileGenerator";

const SUFFIX = "DTO";

export default async function (options) {

    const {asyncapi} = options;

    const dtoPackage = options.params.dtoPackage;
    const packageName = options.params.package;
    const outputDir = options.params.outputDir;

    const packagesForServices = options.params.packageNamesByServices;

    const javaGenerator = new CustomJavaGenerator(options.id, options.modelsSuffix);
    const modelFilesGenerator = new ModelFilesGenerator();

    const channelsFileGenerator = new ChannelsFileGenerator();
    const endpointFileGenerator = new EndpointFileGenerator();
    const endpointFilesGenerator = new EndpointFilesGenerator(endpointFileGenerator);

    const modelsGenerator = new ModelsGenerator(javaGenerator);
    const dtoGenerator = new DtoGenerator(modelFilesGenerator);

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
            }, packagesForServices),
            ...(await dtoGenerator.generate(asyncapi, SUFFIX, packageName, packagesForServices))
        ];


    const writer = new FilesWriter();
    await writer.write(generatedModels, outputDir, true);
    return [];
}