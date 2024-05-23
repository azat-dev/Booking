import Bus from "../../../domain/utils/Bus.ts";
import PresentationCommandHandlersConfig from "./PresentationCommandHandlersConfig.ts";

class PresentationConfig {

    public constructor(
        private readonly commandHandlers: PresentationCommandHandlersConfig,
        private readonly bus: Bus
    ) {
        this.registerCommandHandlers();
    }

    private registerCommandHandlers = (): void => {

        this.bus.subscribe(async command => {

            const handler = this.commandHandlers.getHandlerForCommand(command);
            if (!handler) {
                return;
            }

            console.log(`%cPRESENTATION/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
            handler.execute(command);
        });
    }
}

export default PresentationConfig;