export class ConverterJsonSchemaToAvro {

    constructor(modelsSuffix, namespace, undefinedType, baseNameSpace, getServicePackage) {
        this._modelsSuffix = modelsSuffix;
        this._namespace = namespace;
        this._undefinedType = undefinedType;
        this._getServicePackage = getServicePackage;
        this._baseNameSpace = baseNameSpace;
    }

    _getNamespace = (schema) => {
        let namespace = this._namespace;
        if (schema["x-service"]) {
            namespace = this._baseNameSpace + '.' + this._getServicePackage(schema["x-service"]);
        }
        return namespace;
    }

    convert = (schema) => {

        const namespace = this._getNamespace(schema);

        if (!schema.title) {
            console.log('Schema without title', schema);
        }

        const avroSchema = {
            type: 'record',
            namespace,
            name: schema.title + this._modelsSuffix,
            fields: []
        };

        const {fields, internalTypes} = this._convertPropertiesToFields(schema)
        avroSchema.fields = fields;

        return {
            schema: avroSchema,
            otherTypes: internalTypes
        };
    }

    _convertPropertiesToFields = (schema) => {

        const properties = schema.properties;
        const internalTypes = [];
        const fields = [];
        const required = schema.required || [];

        for (let key in properties) {
            const property = properties[key];
            property.isRequired = required.includes(key);

            const result = this._convertProperty(key, property);
            fields.push(result.field);
            internalTypes.push(...result.internalTypes);
        }

        return {
            internalTypes,
            fields
        }
    }

    _convertProperty = (key, property) => {

        return this._convertPropertyWithOptionals(
            key,
            property,
            (key, property) => this._convertPropertyWithNullable(
                key,
                property,
                this._convertPropertyInternal
            )
        );
    }

    _convertPropertyInternal = (key, property) => {

        if (property.allOf) {
            return this._convertAllOfProperty(key, property);
        }

        if (property.type === 'object') {
            return this._convertObjectProperty(key, property);
        }

        if (property.type === 'string' && property.enum) {

            if (property.title === 'FailedGetListingPublicDetailsByIdErrorCode') {
                debugger;
            }
            const typeName = property.title + this._modelsSuffix;
            const fullType = this._getNamespace(property) + '.' + typeName;
            let namespace = this._getNamespace(property);

            return {
                field: {
                    name: key,
                    type: fullType,
                },
                internalTypes: [
                    {
                        name: typeName,
                        namespace,
                        type: 'enum',
                        symbols: property.enum
                    }
                ]
            };
        }

        const result = this._convertSimpleProperty(key, property);
        return {
            field: result.field,
            internalTypes: result.internalTypes
        };
    }

    _convertPropertyWithNullable = (key, property, convert) => {

        const isNullable = property.anyOf && property.anyOf.some(p => p.type === 'null');
        if (!isNullable) {
            return convert(key, property);
        }

        const result = convert(key, property.anyOf.find(p => p.type !== 'null'));
        return {
            field: {
                name: key,
                type: ['null', result.field.type],
                default: null
            },
            internalTypes: result.internalTypes
        }
    }

    _convertPropertyWithOptionals = (key, property, convert) => {

        const result = convert(key, property);
        if (property.isRequired) {
            return result;
        }

        return {
            field: {
                name: key,
                type: [this._undefinedType, result.field.type]
            },
            internalTypes: result.internalTypes
        }
    }

    _convertAllOfProperty = (key, property) => {

        if (property.allOf.length === 1) {
            const result = this._convertProperty(key, {...property.allOf[0], title: property.title});
            return {
                field: {
                    name: key,
                    type: this._getNamespace(property)  + '.' + property.title + this._modelsSuffix
                },
                internalTypes: result.internalTypes
            }
        }

        const internalTypes = [];

        const fields = property.allOf.reduce((acc, item) => {

            const result = this._convertPropertiesToFields(item);
            internalTypes.push(...result.internalTypes);

            acc.push(
                ...result.fields
            );

            return acc;
        }, []);

        const typeName = property.title + this._modelsSuffix;
        const namespace = this._getNamespace(property);
        const fullTypeName = namespace + '.' + typeName;

        internalTypes.push({
            name: typeName,
            namespace,
            type: 'record',
            fields
        });

        return {
            field: {
                name: key,
                type: fullTypeName
            },
            internalTypes
        }
    }

    _convertObjectProperty = (key, property) => {
        if (property.additionalProperties && property.additionalProperties.type) {
            return {
                field: {
                    name: property.title + this._modelsSuffix,
                    type: {
                        type: 'map',
                        values: this._mapSimpleType(property.additionalProperties.type)
                    }
                },
                internalTypes: []
            }
        }

        if (!property.title) {
            const result = this._convertPropertiesToFields(property);
            return {
                field: {
                    name: key,
                    type: "record",
                    fields: result.fields

                },
                internalTypes: result.internalTypes
            };
        }

        const namespace = this._getNamespace(property);

        const result = this.convert(property);

        return {
            field: {
                name: key,
                type: `${namespace}.${property.title}${this._modelsSuffix}`
            },
            internalTypes: [result.schema, ...result.otherTypes]
        }
    }

    _mapSimpleType = (type) => {

        switch (type) {
            case 'string':
                return 'string';
            case 'integer':
                return 'int';
            case 'number':
                return 'double';
            case 'boolean':
                return 'boolean';
            case 'null':
                return 'null';
            default:
                debugger;
                return type;
        }
    }

    _convertSimpleProperty = (key, property) => {

        if (property.type === 'array') {
            const result = this._convertProperty(key, property.items);
            return {
                field: {
                    name: key,
                    type: {
                        type: 'array',
                        items: result.field.type
                    }
                },
                internalTypes: result.internalTypes
            }
        }

        let type = this._mapSimpleType(property.type);

        if (property.format === 'uuid') {
            type = {
                type: 'string',
                logicalType: 'uuid'
            }
        }

        return {
            field: {
                name: key,
                type
            },
            internalTypes: []
        };
    }
}