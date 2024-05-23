import Subject from "../../utils/binding/Subject.ts";
import DialogsConfig from "./DialogsConfig.ts";
import Bus from "../../../domain/utils/Bus.ts";
import value from "../../utils/binding/value.ts";
import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM.ts";
import LoginDialogDidClose from "../../../domain/auth/events/LoginDialogDidClose.ts";
import SignUpDialogVM from "../../dialogs/sign-up-dialog/SignUpDialogVM.ts";
import SignUpDialogDidClose from "../../../domain/auth/events/SignUpDialogDidClose.ts";
import Command from "../../../domain/utils/Command.ts";
import OpenLoginDialog from "../../commands/OpenLoginDialog.ts";
import OpenedLoginDialog from "../../events/OpenedLoginDialog.ts";
import OpenSignUpDialog from "../../commands/OpenSignUpDialog.ts";
import OpenedSignUpDialog from "../../events/OpenedSignUpDialog.ts";
import CloseDialog from "../../commands/CloseDialog.ts";
import AppEvent from "../../../domain/utils/AppEvent.ts";
import {DialogVM} from "../AppVM.tsx";

class DialogsCommandHandlersConfig {

    public constructor(
        public readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: DialogsConfig,
        private readonly bus: Bus
    ) {
    }

    public closeDialogs = () => {
        const exisitingDialogType = this.activeDialog.value?.type;
        if (!exisitingDialogType) {
            return;
        }

        this.activeDialog.set(null);

        switch (exisitingDialogType) {
            case LoginDialogVM.type:
                this.bus.publish(new LoginDialogDidClose());
                return;
            case SignUpDialogVM.type:
                this.bus.publish(new SignUpDialogDidClose());
                return;
        }
    }

    private handleOpenDialog = (
        vm: () => any,
        openedEvent: AppEvent
    ) => {
        this.activeDialog.set(vm());
        this.bus.publish(openedEvent);
    }

    public handle = (event: Command) => {

        switch (event.constructor) {
            case OpenLoginDialog: {
                this.handleOpenDialog(this.dialogs.loginDialog, new OpenedLoginDialog());
                return;
            }

            case OpenSignUpDialog: {
                this.handleOpenDialog(this.dialogs.signUpDialog, new OpenedSignUpDialog());
                return;
            }

            case CloseDialog:
                this.closeDialogs();
                return;
        }
    }
}

export default DialogsCommandHandlersConfig;