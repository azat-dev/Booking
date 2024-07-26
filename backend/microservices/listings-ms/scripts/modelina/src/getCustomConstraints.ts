import {Constraints, JavaOptions} from "@asyncapi/modelina";
import capitalize from "./capitalize";

const getCustomConstraints = (suffix: string): Partial<Constraints<JavaOptions>> => {
    return {
        modelName: (params) => {
            const {modelName} = params;
            return capitalize(modelName)
                .replaceAll("/", "")
                .replaceAll("\\", "") + suffix;
        }
    };
}

export default getCustomConstraints;