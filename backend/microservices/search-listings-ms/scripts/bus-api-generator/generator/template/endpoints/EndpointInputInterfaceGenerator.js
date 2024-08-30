import {getInputTypeNameForEndpoint, getInputTypes} from "./utils";

export class EndpointInputInterfaceGenerator {

    constructor(basePackageName, modelsSuffix, getPackageForService) {
        this._basePackageName = basePackageName;
        this._modelsSuffix = modelsSuffix;
        this._getPackageForService = getPackageForService;
    }

    _renderContent = (packageName, interfaceName) => {

        return `
            package ${packageName};
            
            public interface ${interfaceName} {}
        `.trim();
    }

    generate = (operation, packageName) => {

        const operationId = operation.id();
        const inputTypes = getInputTypes(operation, this._modelsSuffix, this._getPackageForService);
        if (inputTypes.length === 1) {
            return null;
        }

        const pckg = `${this._basePackageName}.dto.${this._getPackageForService()}`

        const interfaceName = getInputTypeNameForEndpoint(operationId, inputTypes, this._modelsSuffix)

        return {
            fileName: `${interfaceName}.java`,
            packageName: packageName,
            content: this._renderContent(pckg, interfaceName)
        }
    }
}