import {capitalize, removeSlashes} from "./utils";
import {
    ConstrainedObjectModel,
    ConstrainedReferenceModel,
    JAVA_JACKSON_PRESET,
    JavaGenerator
} from "@asyncapi/modelina";

export class CustomJavaGenerator {

    constructor(
        id,
        modelsSuffix
    ) {
        this._generator = new JavaGenerator(
            {
                constraints: this._getCustomConstraints(id, modelsSuffix),
                typeMapping: this._getCustomTypeMappings(),
                presets: [
                    this._getCustomJavaPreset(modelsSuffix),
                    this._getCustomJacksonPreset()
                ]
            }
        );
    }

    _getCustomConstraints = (id, modelsSuffix) => {
        return {
            modelName: (params) => {
                const {modelName} = params;
                return capitalize(removeSlashes(modelName + modelsSuffix));
            }
        };
    }

    _getCustomJacksonPreset = () => {

        const JACKSON_ANNOTATION_DEPENDENCY =
            'import com.fasterxml.jackson.annotation.*;';

        return {
            ...JAVA_JACKSON_PRESET,
            union: {
                additionalContent: ({renderer, model}) => {

                    renderer.dependencyManager.addDependency(JACKSON_ANNOTATION_DEPENDENCY);

                    const blocks = [];

                    if (model.options.discriminator) {
                        const {discriminator} = model.options;
                        blocks.push(
                            renderer.renderAnnotation('JsonTypeInfo', {
                                use: 'JsonTypeInfo.Id.NAME',
                                include: 'JsonTypeInfo.As.EXISTING_PROPERTY',
                                property: `"${discriminator.discriminator}"`,
                                visible: 'true'
                            })
                        );

                        const types = model.union
                            .map((union) => {
                                if (
                                    union instanceof ConstrainedReferenceModel &&
                                    union.ref instanceof ConstrainedObjectModel
                                ) {
                                    const discriminatorProp = Object.values(
                                        union.ref.properties
                                    ).find(
                                        (model) =>
                                            model.unconstrainedPropertyName ===
                                            discriminator.discriminator
                                    );

                                    if (discriminatorProp?.property.options.const) {
                                        return `  @JsonSubTypes.Type(value = ${union.name}.class, name = "${discriminatorProp.property.options.const.originalInput}")`;
                                    }
                                }

                                return `  @JsonSubTypes.Type(value = ${union.name}.class, name = "${union.name}")`;
                            })
                            .join(',\n');

                        blocks.push(
                            renderer.renderAnnotation('JsonSubTypes', `{\n${types}\n}`)
                        );
                    } else {
                        blocks.push(
                            renderer.renderAnnotation('JsonTypeInfo', {
                                use: 'JsonTypeInfo.Id.DEDUCTION'
                            })
                        );

                        const types = model.union
                            .map(
                                (union) =>
                                    `  @JsonSubTypes.Type(value = ${union.name}.class, name = "${union.name}")`
                            )
                            .join(',\n');

                        blocks.push(
                            renderer.renderAnnotation('JsonSubTypes', `{\n${types}\n}`)
                        );
                    }

                    const content = "public static interface TypeInfo {}"
                    return renderer.renderBlock([...blocks, content]);
                }
            }
        }
    };

    _getCustomTypeMappings = () => {

        return {
            String: context => {

                const format = context?.constrainedModel?.options?.format;

                if (format === 'uuid') {
                    context.dependencyManager.addDependency("import java.util.UUID;");
                    return 'UUID';
                }

                if (format === 'date-time' || format === 'datetime') {
                    return 'String';
                }

                if (format === 'uri') {
                    context.dependencyManager.addDependency('import java.net.URI;');
                    return 'URI';
                }

                return "String";
            },
            Integer: context => {
                const format = context?.constrainedModel?.options?.format;

                if (format === 'int64') {
                    return 'Long';
                }

                return "Integer";
            }
        };
    }

    _getCustomJavaPreset = (modelSuffix) => {

        const JSON_NULLABLE_IMPORT = 'import org.openapitools.jackson.nullable.JsonNullable;';

        const OPTIONAL_IMPORT = 'import java.util.Optional;';

        const CLASS_ANNOTATIONS =
            `
@lombok.Builder(toBuilder = true)
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
 `.trim();

        return {
            class: {
                property(opt) {
                    const {property, model} = opt;
                    const isRequired = property.required;
                    let wrapper = "";
                    let propertyType = property.property.type;
                    let initializer = property.property.options.const?.value ? ` = ${property.property.options.const.value}` : "";

                    if (!isRequired) {
                        wrapper = "JsonNullable";
                        if (initializer) {
                            initializer = ` = JsonNullable.of(${initializer})`;
                        } else {
                            initializer = ` = JsonNullable.undefined()`;
                        }

                        opt.renderer.dependencyManager.addDependency(JSON_NULLABLE_IMPORT);
                    }

                    if (property.property.options.isNullable) {
                        wrapper = "Optional";
                        if (initializer) {
                            initializer = ` = Optional.ofNullable(${initializer})`;
                        } else {
                            initializer = ` = Optional.empty()`;
                        }

                        const foundTypeItem = property.property.originalInput.anyOf.find((item) => item.type !== 'null');
                        opt.renderer.dependencyManager.addDependency(OPTIONAL_IMPORT);
                        propertyType = foundTypeItem.title + modelSuffix;
                    }

                    if (model.options.isExtended) {
                        return '';
                    }

                    if (property.property.options.const?.value) {
                        if (wrapper) {
                            return `private final ${wrapper}<${propertyType}> ${property.propertyName}${initializer};`;
                        }

                        return `private final ${propertyType} ${property.propertyName}${initializer};`;
                    }

                    if (wrapper) {
                        return `private ${wrapper}<${propertyType}> ${property.propertyName}${initializer};`;
                    }

                    return `private ${propertyType} ${property.propertyName};`;
                },
                self(opt) {
                    const {content} = opt;
                    return `${CLASS_ANNOTATIONS}\n${content}`;
                },
                getter(opt) {
                    return "";
                },
                setter(opt) {
                    return "";
                }
            }
        }
    };

    generateCompleteModels = async (input, completeOptions) => {

        return this._generator.generateCompleteModels(
            input,
            completeOptions
        )
    }
}