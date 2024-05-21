/**
 * The playground does not support imports and exports by default.
 * The plugin strips all imports except 'typescript' and makes sure its available for the transformer
 */
import ts from 'typescript';

/**
 * Any top level block statement with a `@transform` tag will be input for the transformer
 * @transform
 */
{
    // This example transformer will evaluate the following expression(s) at compile time
    const result = (8 / 4) * 2 + 2 - 5;
}

/**
 * Any top level block statement with a `@transform` tag will be input for the transformer
 * @transform
 */
{
    class Command {

    }

    class MyCommand extends Command {

    }
}

/**
 * The Program transformer signature is based on https://github.com/cevek/ttypescript#program
 */
const programTransformer = (program: ts.Program) => {

    function transformClass(node: ts.ClassDeclaration, context: ts.TransformationContext): ts.ClassDeclaration {
        // Check if class implements Command interface
        if (!node.heritageClauses) {
            return node;
        }

        const implementsCommand = node.heritageClauses.some((heritage) =>
            heritage.types.some((type) => ts.isIdentifier(type.expression) && type.expression.getText() === "Command")
        );

        if (!implementsCommand) {
            return node;
        }

        // Add static TYPE property
        const typeName = node.name!.getText();
        const staticType = createStaticTypeProperty(typeName);
        return context.factory.updateClassDeclaration(
            node,
            node.modifiers,
            node.name,
            node.typeParameters,
            node.heritageClauses,
            [...node.members, staticType, createGetter(typeName)]
        );
    }

    function camelToScreamingSnake(camelCase: string): string {
        return camelCase.replace(/[A-Z]/g, match => '_' + match)
            .toUpperCase()
            .replace(/^_/, '');
    }

    function createStaticTypeProperty(name: string): ts.PropertyDeclaration {
        const screamingSnakeCase = camelToScreamingSnake(name);
        const typeNode = ts.factory.createLiteralTypeNode(ts.factory.createStringLiteral(screamingSnakeCase));

        return ts.factory.createPropertyDeclaration(
            [
                ts.factory.createToken(ts.SyntaxKind.PublicKeyword),
                ts.factory.createToken(ts.SyntaxKind.StaticKeyword),
                ts.factory.createToken(ts.SyntaxKind.ReadonlyKeyword)
            ],
            "TYPE",
            undefined,
            undefined,
            ts.factory.createStringLiteral(screamingSnakeCase)
        );
    }

    function createGetter(name: string) {
        return ts.factory.createGetAccessorDeclaration(
            [ts.factory.createToken(ts.SyntaxKind.PublicKeyword)],
            ts.factory.createIdentifier("type"),
            [],
            undefined,
            ts.factory.createBlock(
                [ts.factory.createReturnStatement(ts.factory.createPropertyAccessExpression(
                    ts.factory.createIdentifier(name),
                    ts.factory.createIdentifier("TYPE11111111")
                ))],
                true
            )
        );
    }

    return (context: ts.TransformationContext) => {
        return (sourceFile: ts.SourceFile) => {
            const visitor = (node: ts.Node): ts.Node => {
                node = ts.visitEachChild(node, visitor, context);

                if (ts.isClassDeclaration(node)) {
                    console.log("VISITOR");
                    return transformClass(node, context);
                }

                return node;
            }
            return ts.visitNode(sourceFile, visitor);
        }
    }
}

/**
 * Anything other than a node transformer will need to specifiy its type as an export
 */
export const type = 'program';

/**
 * The default export should be your transformer
 */
export default programTransformer;
