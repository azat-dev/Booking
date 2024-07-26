import {JavaOptions} from "@asyncapi/modelina/lib/types/generators/java/JavaGenerator";
import {JavaPreset} from "@asyncapi/modelina/lib/types/generators/java/JavaPreset";
import {ConstrainedObjectModel, ConstrainedReferenceModel, JAVA_JACKSON_PRESET} from "@asyncapi/modelina";


const JACKSON_ANNOTATION_DEPENDENCY =
    'import com.fasterxml.jackson.annotation.*;';

const getCustomJacksonPreset = () : JavaPreset<JavaOptions> => ({
    ...JAVA_JACKSON_PRESET,
    union: {
        additionalContent: ({renderer, model}) => {

            renderer.dependencyManager.addDependency(JACKSON_ANNOTATION_DEPENDENCY);

            const blocks: string[] = [];

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
});

export default getCustomJacksonPreset;