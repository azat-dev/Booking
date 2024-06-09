import Bus from "../../domain/utils/Bus";
import CommonDialogsConfig from "../app/config/presentation/common/CommonDialogsConfig.ts";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import OpenLoginDialog from "../commands/OpenLoginDialog";
import CloseDialog from "../commands/CloseDialog";
import Command from "../../domain/utils/Command";
import OpenSignUpDialog from "../commands/OpenSignUpDialog";
import OpenedLoginDialog from "../events/OpenedLoginDialog";
import OpenedSignUpDialog from "../events/OpenedSignUpDialog";
import LoginDialogVM from "../dialogs/login-dialog/LoginDialogVM";
import LoginDialogDidClose from "../../domain/auth/events/LoginDialogDidClose";
import SignUpDialogVM from "../dialogs/sign-up-dialog/SignUpDialogVM";
import SignUpDialogDidClose from "../../domain/auth/events/SignUpDialogDidClose";

class DialogsStore {

    public readonly activeDialog: Subject<any | null>;

    public constructor(
        private readonly dialogs: CommonDialogsConfig,
        private readonly bus: Bus
    ) {
        this.activeDialog = value(null);
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

    public handle = (command: Command) => {

        switch (command.type) {
            case OpenLoginDialog.type: {
                const vm = this.dialogs.loginDialog();
                this.activeDialog.set(vm);
                this.bus.publish(new OpenedLoginDialog().withSender(command.senderId));
                return;
            }

            case OpenSignUpDialog.type: {
                const vm = this.dialogs.signUpDialog();
                this.activeDialog.set(vm);
                this.bus.publish(new OpenedSignUpDialog().withSender(command.senderId));
                return;
            }

            case CloseDialog.type:
                this.closeDialogs();
                return;
        }
    }
}

export default DialogsStore;