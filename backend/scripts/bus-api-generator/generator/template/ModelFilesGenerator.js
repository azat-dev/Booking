import {CustomJavaGenerator} from "./CustomJavaGenerator";

export class ModelFilesGenerator {
    constructor(getPackageForService) {
        this._getPackageForService = getPackageForService;
    }
    generate = async (input, options, packagesForServices) => {
        const javaGenerator = new CustomJavaGenerator(
            options.id,
            options.modelsSuffix
        );

        return javaGenerator.generateCompleteModels(
            input,
            {packageName: `${options.packageName}.dto.${this._getPackageForService(options.serviceId)}`}
        )
    }
}