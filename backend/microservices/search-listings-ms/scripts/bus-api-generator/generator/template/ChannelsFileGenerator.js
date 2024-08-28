import {convertChannelIdToConstantName} from "./utils";

export class ChannelsFileGenerator {

    _getChannelConstantNames = (asyncapi) => {
        const result = [];

        asyncapi.forEach(channel => {
            if (channel.address() === 'null' || channel.id() === 'null') {
                return;
            }

            result.push(
                {
                    id: convertChannelIdToConstantName(channel.id()),
                    value: channel.address()
                }
            );
        });

        return result;
    }

    generate = (packageName, channels) => {

        const items = this._getChannelConstantNames(channels);

        const content = `
package ${packageName};

public enum Channels {
    ${items.map(item => `${item.id}("${item.value}"),`).join("\n")};

    private final String value;

    Channels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
`.trim()

        return {
            fileName: 'Channels.java',
            content,
            packageName: `${packageName}`
        }
    }
}