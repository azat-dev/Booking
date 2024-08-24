import {CustomJavaGenerator} from "./CustomJavaGenerator";

export class ModelFilesGenerator {
    generate = async (input, options, packagesForServices) => {
        const javaGenerator = new CustomJavaGenerator(
            options.id,
            options.modelsSuffix
        );

        return javaGenerator.generateCompleteModels(
            input,
            {packageName: `${options.packageName}.dto.${packagesForServices[options.serviceId]}`}
        )
    }
}