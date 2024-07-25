import {
    ConstrainedObjectModel,
    ConstrainedReferenceModel,
    JAVA_JACKSON_PRESET,
    JavaFileGenerator
} from '@asyncapi/modelina';
import path from 'path';
import yaml from 'js-yaml';
import * as fs from "node:fs";
import yargs from 'yargs';
import {hideBin} from 'yargs/helpers';

const argv = yargs(hideBin(process.argv))
    .option('package', {
        alias: 'p',
        description: 'The package name for the generated models',
        type: 'string',
        demandOption: true, // Makes this argument required
    })
    .option('input', {
        alias: 'i',
        description: 'The path to the input AsyncAPI file',
        type: 'string',
        demandOption: true, // Makes this argument required
    })
    .option('output', {
        alias: 'o',
        description: 'The path to the output directory for the generated models',
        type: 'string',
        demandOption: true, // Makes this argument required
    })
    .help()
    .alias('help', 'h')
    .argv;

const replaceSeparator = (filepath: string) => {
    return filepath.replace("/", path.sep).replace("\\", path.sep);

}

const INPUT_FILE = replaceSeparator((argv as any).input);
const OUTPUT_DIR = replaceSeparator((argv as any).output);
const PACKAGE_NAME = (argv as any).package;
const MODEL_DIR = replaceSeparator(`${PACKAGE_NAME.split(".").join("/")}`);
const FINAL_OUTPUT_PATH = path.resolve(__dirname, OUTPUT_DIR, MODEL_DIR);

console.debug("Generating models for package:", PACKAGE_NAME);
console.debug("Using input file:", INPUT_FILE);
console.debug("Using output file:", OUTPUT_DIR);

// Setup the generator and all it's configurations

const generator = new JavaFileGenerator({
    presets: [
        {
            class: {
                property(opt) {
                    const {property, model} = opt;
                    const isRequired = property.required;
                    let wrapper = "";
                    let propertyType = property.property.type;

                    if (!isRequired) {
                        wrapper = "JsonNullable";
                        opt.renderer.dependencyManager.addDependency("import org.openapitools.jackson.nullable.JsonNullable;");
                    }

                    if (property.property.options.isNullable) {
                        wrapper = "Optional";
                        const foundTypeItem = property.property.originalInput.anyOf.find((item: any) => item.type !== 'null');
                        opt.renderer.dependencyManager.addDependency("import java.util.Optional;");
                        propertyType = foundTypeItem.title + "DTO";
                    }

                    if (model.options.isExtended) {
                        return '';
                    }

                    if (property.property.options.const?.value) {
                        if (wrapper) {
                            return `private final ${wrapper}<${propertyType}> ${property.propertyName} = ${wrapper}.of(${property.property.options.const.value});`;
                        }

                        return `private final ${propertyType} ${property.propertyName} = ${property.property.options.const.value};`;
                    }

                    if (wrapper) {
                        return `private ${wrapper}<${propertyType}> ${property.propertyName};`;
                    }

                    return `private ${propertyType} ${property.propertyName};`;
                },
                getter(opt) {
                    return "";
                },
                setter(opt) {
                    return "";
                },
                self(opt) {
                    const {content} = opt;
                    const CLASS_ANNOTATIONS = `@lombok.Builder(toBuilder = true)\n@lombok.AllArgsConstructor\n@lombok.NoArgsConstructor\n@lombok.Getter\n@lombok.Setter\n@lombok.EqualsAndHashCode`.trim();
                    return `${CLASS_ANNOTATIONS}\n${content}`;
                }
            }
        },
        {
            ...JAVA_JACKSON_PRESET,
            union: {
                additionalContent: ({renderer, model}) => {

                    const JACKSON_ANNOTATION_DEPENDENCY =
                        'import com.fasterxml.jackson.annotation.*;';

                    renderer.dependencyManager.addDependency(JACKSON_ANNOTATION_DEPENDENCY);

                    const blocks: string[] = [];

                    if (model.options.discriminator) {
                        const { discriminator } = model.options;
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
        },
    ],
    constraints: {
        modelName: (options) => {
            const capitalized = options.modelName.charAt(0).toUpperCase() + options.modelName.slice(1);
            return capitalized
                .replace("/", "")
                .replace("\\", "") + "DTO";
        }
    },
    typeMapping: {
        String: context => {

            const format = context?.constrainedModel?.options?.format;

            if (format === 'uuid') {
                context.dependencyManager.addDependency("import java.util.UUID;");
                return 'UUID';
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
    }
});

// Load the input from file, memory, or remotely.
// Here we just use a local AsyncAPI file

const input = yaml.load(fs.readFileSync(INPUT_FILE, 'utf8'));
const inputDir = path.resolve(path.dirname(INPUT_FILE));
console.log("Starting model generation...");

process.chdir(inputDir);
generator.generateToFiles(input, FINAL_OUTPUT_PATH, {
    packageName: PACKAGE_NAME
})
    .then((result) => {
        console.log("Model generation completed successfully.");
    })
    .catch((error) => {
        console.error("Model generation failed:", JSON.stringify(error, null, 2));
        throw error;
    });