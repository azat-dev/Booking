import {Constraints, JavaOptions} from "@asyncapi/modelina";
import capitalize from "./capitalize";

const getCustomConstraints = (suffix: string): Partial<Constraints<JavaOptions>> => {
    return {
        modelName: ({modelName}) => {
            return capitalize(modelName)
                .replace("/", "")
                .replace("\\", "") + suffix;
        }
    };
}

export default getCustomConstraints;