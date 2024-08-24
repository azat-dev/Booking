export class EndpointFilesGenerator {

    constructor(endpointFileGenerator) {
        this._endpointFileGenerator = endpointFileGenerator;
    }

    generate = (doc, options, packagesByServices) => {
        return doc.operations()
            .filterByReceive()
            .map(operation => this._endpointFileGenerator.generate(operation, options, packagesByServices));
    }
}