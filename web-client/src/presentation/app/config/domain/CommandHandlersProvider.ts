import Command from "../../../../domain/utils/Command.ts";
import Handler from "../../../../domain/utils/Handler.ts";

export default interface CommandHandlersProvider {

    getHandlerForCommand(command: Command): Handler;
}