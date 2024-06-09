import Subject from "../../../../utils/binding/Subject.ts";
import CommonDialogsConfig from "./CommonDialogsConfig.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import Command from "../../../../../domain/utils/Command.ts";
import {DialogVM} from "../../../AppVM.tsx";
import CommandHandlersProvider from "../../domain/CommandHandlersProvider.ts";
import Handler from "../../../../../domain/utils/Handler.ts";
import CloseDialog from "../../../../commands/CloseDialog.ts";
import HandleCloseDialogs from "../../../../../domain/auth/handlers/HandleCloseDialogs.ts";
import OpenLoginDialog from "../../../../commands/OpenLoginDialog.ts";
import HandleOpenLoginDialog from "../../../../../domain/auth/handlers/HandleOpenLoginDialog.ts";
import OpenSignUpDialog from "../../../../commands/OpenSignUpDialog.ts";
import HandleOpenSignUpDialog from "../../../../../domain/auth/handlers/HandleOpenSignUpDialog.ts";

class DialogsCommandHandlersProvider implements CommandHandlersProvider {

    private readonly handlersByCommands: Record<string, Handler>;

    public constructor(
        public readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly bus: Bus
    ) {
        this.handlersByCommands = {
            [CloseDialog.name]: new HandleCloseDialogs(activeDialog, dialogs, bus),
            [OpenLoginDialog.name]: new HandleOpenLoginDialog(activeDialog, dialogs, bus),
            [OpenSignUpDialog.name]: new HandleOpenSignUpDialog(activeDialog, dialogs, bus),
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlersByCommands[command.constructor.name];
    }
}

export default DialogsCommandHandlersProvider;