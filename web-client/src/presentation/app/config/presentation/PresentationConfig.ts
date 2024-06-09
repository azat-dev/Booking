import Bus from "../../../../domain/utils/Bus.ts";
import ProfileCommandsHandlersProvider from "./ProfileCommandsHandlersProvider.ts";
import CommandHandlersProvider from "../domain/CommandHandlersProvider.ts";

class PresentationConfig {

    private readonly commandHandlersProviders: CommandHandlersProvider[] = [];

    public constructor(bus: Bus) {

        bus.subscribe(async command => {

            this.commandHandlersProviders.forEach(provider => {

                const handler = provider.getHandlerForCommand(command);
                if (!handler) {
                    return;
                }

                console.log(`%cPRESENTATION/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
                handler.execute(command);

            });
        });
    }

    public addCommandHandlersProvider = (provider: CommandHandlersProvider) => {
        this.commandHandlersProviders.push(provider);
    }
}

export default PresentationConfig;