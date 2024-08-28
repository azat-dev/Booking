export class EndpointFilesGenerator {

    constructor(endpointFileGenerator, endpointInputInterfaceGenerator) {
        this._endpointFileGenerator = endpointFileGenerator;
        this._endpointInputInterfaceGenerator = endpointInputInterfaceGenerator;
    }

    generate = (doc, options) => {

        const result = doc.operations()
            .filterByReceive()
            .map(operation => this._endpointFileGenerator.generate(operation, options));

        doc.operations()
            .filterByReceive()
            .forEach((operation) => {

                const interfaceData = this._endpointInputInterfaceGenerator.generate(operation, 'XXXXX');
                if (interfaceData) {
                    result.push(interfaceData);
                }
            })

        return result;
    }
}