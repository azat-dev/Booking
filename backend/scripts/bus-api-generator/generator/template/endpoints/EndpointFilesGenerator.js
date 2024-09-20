import {getChannelServiceId} from "../utils";

export class EndpointFilesGenerator {

    constructor(endpointFileGenerator, endpointInputInterfaceGenerator, packageName, getServicePackage) {
        this._endpointFileGenerator = endpointFileGenerator;
        this._endpointInputInterfaceGenerator = endpointInputInterfaceGenerator;
        this._packageName = packageName;
        this._getServicePackage = getServicePackage;
    }

    generate = (doc, options) => {

        const result = doc.operations()
            .filterByReceive()
            .map(operation => this._endpointFileGenerator.generate(operation, options));

        doc.operations()
            .filterByReceive()
            .forEach((operation) => {

                const inputChannels = operation.channels?.();

                if (inputChannels.length !== 1) {
                    console.error("Operation doesn't have exactly one channel", JSON.stringify(operation, null, 2));
                    throw new Error("Operation doesn't have exactly one channel");
                }

                const inputChannel = inputChannels[0];

                const serviceId = getChannelServiceId(inputChannel);
                const servicePackage = this._getServicePackage(serviceId);
                const pckg = `${this._packageName}.dto.${servicePackage}`;

                const interfaceData = this._endpointInputInterfaceGenerator.generate(operation, pckg);
                if (interfaceData) {
                    result.push(interfaceData);
                }
            })

        return result;
    }
}