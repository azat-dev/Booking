import fs from "node:fs";

const path = require('path');
const Generator = require("@asyncapi/generator");

import yargs from 'yargs';
import {hideBin} from 'yargs/helpers';

const argv = yargs(hideBin(process.argv))
    .option('package', {
        alias: 'p',
        description: 'The package name for the generated models',
        type: 'string',
        demandOption: true, // Makes this argument required
    })
    .option('dto-package', {
        alias: 'dto',
        description: 'The package name for the generated DTOs',
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

const INPUT_FILE = path.resolve(replaceSeparator((argv as any).input));
const OUTPUT_DIR = path.resolve(replaceSeparator((argv as any).output));
const PACKAGE_NAME = (argv as any).package;
const MODEL_DIR = replaceSeparator(`${PACKAGE_NAME.split(".").join("/")}`);
const FINAL_OUTPUT_PATH = path.resolve(OUTPUT_DIR, MODEL_DIR);

console.debug("Generating models for package:", PACKAGE_NAME);
console.debug("Using input file:", INPUT_FILE);
console.debug("Using output dir:", OUTPUT_DIR);


const generator = new Generator(
    path.resolve(__dirname, 'generator'),
    FINAL_OUTPUT_PATH,
    {
        forceWrite: true,
        templateParams: {
            package: (argv as any).package,
            dtoPackage: (argv as any).dto
        }
    });

const inputDir = path.resolve(path.dirname(INPUT_FILE));
console.log("Starting model generation...");

console.log("Resolved directory to:", inputDir);
process.chdir(inputDir);

generator.generateFromFile(INPUT_FILE, undefined, {forceWrite: true})
    .then(() => console.log('Done!'))
    .catch((e: any) => console.error(e));