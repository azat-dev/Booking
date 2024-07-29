import {JavaOptions} from "@asyncapi/modelina/lib/types/generators/java/JavaGenerator";
import {JavaPreset} from "@asyncapi/modelina/lib/types/generators/java/JavaPreset";

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

const getCustomJavaPreset = (modelSuffix: string) : JavaPreset<JavaOptions> => ({
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

                const foundTypeItem = property.property.originalInput.anyOf.find((item: any) => item.type !== 'null');
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
});

export default getCustomJavaPreset;