import path from "node:path";
import {
    FileHelpers
} from "@asyncapi/modelina";

export class FilesWriter {

    _replaceSeparator = (filepath) => {
        return filepath.replace("/", path.sep).replace("\\", path.sep);
    }

    _getFilePath = (packageName, fileName, outputDirectory) => {

        if (!packageName) {
            return path.resolve(
                outputDirectory,
                `${fileName}`
            );
        }

        return path.resolve(
            outputDirectory,
            this._replaceSeparator(`${packageName.split(".").join("/")}`),
            `${fileName}`
        );
    }

    write = async (generatedModels, outputDirectory, ensureFilesWritten) => {

        for (const outputModel of generatedModels) {

            const filePath = this._getFilePath(outputModel.packageName, outputModel.fileName, outputDirectory);

            await FileHelpers.writerToFileSystem(
                outputModel.content,
                filePath,
                ensureFilesWritten
            );
        }
    }
}