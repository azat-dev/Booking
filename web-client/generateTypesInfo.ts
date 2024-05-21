const ts = require('typescript');
const fs = require('fs');
const path = require('path');


// Function to recursively scan a directory and return all TypeScript file paths
function scanDirectory(dir: string, fileList: string[] = []): string[] {
    const files = fs.readdirSync(dir);
    files.forEach((file: any) => {
        const filePath = path.join(dir, file);
        if (fs.statSync(filePath).isDirectory()) {
            scanDirectory(filePath, fileList);
        } else if (filePath.endsWith('.ts')) {
            fileList.push(filePath);
        }
    });
    return fileList;
}

// Function to parse TypeScript files and extract class names
function parseTypescriptFiles(filePaths: string[], baseClassName: string): Map<string, {filePath: string, name: string}> {
    const classNameMap = new Map<string, {filePath: string, name: string}>();

    filePaths.forEach(filePath => {
        const sourceFile = ts.createSourceFile(filePath, fs.readFileSync(filePath).toString(), ts.ScriptTarget.Latest, true);

        ts.forEachChild(sourceFile, (node: any) => {
            if (ts.isClassDeclaration(node) && node.name) {

                const implementsCommand = node.heritageClauses?.some((heritage: any) =>
                    heritage.types.some((type: any) =>  type?.expression?.getText?.() === baseClassName)
                );

                if (!implementsCommand) {
                    return node;
                }

                classNameMap.set(node.name.getText(), {
                    filePath: filePath,
                    name: node.name.getText()
                });
            }
        });
    });

    return classNameMap;
}

function camelToScreamingSnake(camelCase: string): string {
    return camelCase.replace(/[A-Z]/g, match => '_' + match)
        .toUpperCase()
        .replace(/^_/, '');
}

// Function to generate a TypeScript file with the Map
function generateMapFile(classNameMap: Map<string, { filePath: string, name: string }>, outputPath: string) {

    const imports = Array.from(classNameMap).map(([key, value]) => `import ${key} from "${value.filePath.replace('src/', '../')}`).map(i => (i + ';').replace(".ts;", "\";")).join('\n');

    const content = imports + '\n\n' +
        `const TYPE_INFO: Record<string, string> = {\n` +
        `\t${Array.from(classNameMap).map(([key, value]) => `\n\t\t[${key}.name]: "${camelToScreamingSnake(value.name)}",`).join('\n')}
};

export default TYPE_INFO;`;

    fs.writeFileSync(outputPath, content);
}

// Main function to orchestrate the process
function generateClassMap(projectDir: string, outputPath: string, baseClassName: string) {
    const tsFilePaths = scanDirectory(projectDir);
    const classNameMap = parseTypescriptFiles(tsFilePaths, baseClassName);
    generateMapFile(classNameMap, outputPath);
}

generateClassMap('./src', './src/generated/COMMANDS_TYPE_INFO.ts', "Command");
generateClassMap('./src', './src/generated/EVENTS_TYPE_INFO.ts', "AppEvent");
generateClassMap('./src', './src/generated/HANDLERS_TYPE_INFO.ts', "Handler");
generateClassMap('./src', './src/generated/POLICIES_TYPE_INFO.ts', "Policy");