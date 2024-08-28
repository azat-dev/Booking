export class MessageFileGenerator {

    generate = (packageName) => {
        const content = `
package ${packageName};

import java.time.LocalDateTime;

public record Message<T>(
    String id,
    String type,
    LocalDateTime timestamp,
    T payload
) {
}
`.trim();

        return (
            {
                fileName: 'Message.java',
                content,
                packageName: packageName
            }
        )
    }
}