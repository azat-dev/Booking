import {getInputTypeNameForEndpoint, getInputTypes} from "./utils";

export class EndpointInputInterfaceGenerator {

    constructor(modelsSuffix) {
        this._modelsSuffix = modelsSuffix;
    }

    _renderContent = (interfaceName) => {

        return `
            public interface ${interfaceName} {}
        `.trim();
    }

    generate = (operation, packageName) => {

        const operationId = operation.id();
        const inputTypes = getInputTypes(operation, this._modelsSuffix);
        if (inputTypes.length === 1) {
            return null;
        }
        const interfaceName = getInputTypeNameForEndpoint(operationId, inputTypes, this._modelsSuffix)

        return {
            fileName: `${interfaceName}.java`,
            packageName: packageName,
            content: this._renderContent(interfaceName)
        }
    }
}