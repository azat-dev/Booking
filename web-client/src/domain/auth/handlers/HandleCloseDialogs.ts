import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import CommonDialogsConfig from "../../../presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import LoginDialogVM from "../../../presentation/dialogs/login-dialog/LoginDialogVM.ts";
import LoginDialogDidClose from "../events/LoginDialogDidClose.ts";
import SignUpDialogVM from "../../../presentation/dialogs/sign-up-dialog/SignUpDialogVM.ts";
import SignUpDialogDidClose from "../events/SignUpDialogDidClose.ts";
import CloseDialog from "../../../presentation/commands/CloseDialog.ts";

class HandleCloseDialogs extends Handler {

    public constructor(
        private readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: CloseDialog): Promise<void> => {

        const exisitingDialogType = this.activeDialog.value?.type;
        if (!exisitingDialogType) {
            return;
        }

        this.activeDialog.set(null);

        switch (exisitingDialogType) {
            case LoginDialogVM.type:
                this.bus.publish(new LoginDialogDidClose().withSender(command.senderId));
                return;
            case SignUpDialogVM.type:
                this.bus.publish(new SignUpDialogDidClose().withSender(command.senderId));
                return;
        }
    }
}

export default HandleCloseDialogs;