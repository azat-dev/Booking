import {JavaDependencyManager} from "@asyncapi/modelina/lib/types/generators/java/JavaDependencyManager";
import {JavaOptions, TypeMapping} from "@asyncapi/modelina";

const getCustomTypeMappings = (): Partial<TypeMapping<JavaOptions, JavaDependencyManager>> => {

    return {
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
    };
}

export default getCustomTypeMappings;