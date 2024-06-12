import LoginDialogVM from "../../../../dialogs/login-dialog/LoginDialogVM.ts";
import SignUpDialogVM from "../../../../dialogs/sign-up-dialog/SignUpDialogVM.ts";
import IdentityCommands from "../../domain/IdentityCommands.ts";
import CommonDialogsCommands from "./CommonDialogsCommands.ts";

class CommonDialogsConfig {

    public commonDialogsCommands!: CommonDialogsCommands

    public constructor(
        private readonly identityCommands: IdentityCommands
    ) {
    }

    public loginDialog = () => {
        return new LoginDialogVM(
            this.identityCommands.loginByEmail,
            this.commonDialogsCommands.openSignUpDialog,
            this.commonDialogsCommands.closeDialogs
        );
    }

    public signUpDialog = () => {
        return new SignUpDialogVM(
            this.identityCommands.signUpByEmail,
            this.commonDialogsCommands.closeDialogs,
            this.commonDialogsCommands.openLoginDialog
        );
    }
}

export default CommonDialogsConfig;