import Subject from "../../../../utils/binding/Subject.ts";
import CommonDialogsConfig from "./CommonDialogsConfig.ts";
import Bus, {Emit} from "../../../../../domain/utils/Bus.ts";
import {DialogVM} from "../../../AppVM.tsx";
import CloseDialogs from "../../../../../domain/auth/commands/CloseDialogs.ts";
import OpenLoginDialog from "../../../../../domain/auth/commands/OpenLoginDialog.ts";
import OpenSignUpDialog from "../../../../../domain/auth/commands/OpenSignUpDialog.ts";

class CommonDialogsCommands {

    public constructor(
        public readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly emit: Emit
    ) {
    }

    public closeDialogs = () => {
        return new CloseDialogs(this.activeDialog, this.emit)
            .execute();
    }

    public openLoginDialog = () => {
        return new OpenLoginDialog(this.activeDialog, this.dialogs, this.emit)
            .execute();
    }

    public openSignUpDialog = () => {
        return new OpenSignUpDialog(this.activeDialog, this.dialogs, this.emit)
            .execute();
    }
}

export default CommonDialogsCommands;