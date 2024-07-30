import {
    JavaFileGenerator
} from '@asyncapi/modelina';
import path from 'path';
import yaml from 'js-yaml';
import * as fs from "node:fs";
import yargs from 'yargs';
import {hideBin} from 'yargs/helpers';
import getCustomConstraints from "./getCustomConstraints";
import getCustomTypeMappings from "./getCustomTypeMappings";
import getCustomJavaPreset from "./getCustomJavaPreset";
import getCustomJacksonPreset from "./getCustomJacksonPreset";

const MODELS_SUFFIX = "DTO";

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
        getCustomJavaPreset(MODELS_SUFFIX),
        getCustomJacksonPreset()
    ],
    constraints: getCustomConstraints(MODELS_SUFFIX),
    typeMapping: getCustomTypeMappings()
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