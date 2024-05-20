import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
import Bus, {matchTypes} from "../../../domain/utils/Bus";
import CloseDialog from "../../commands/CloseDialog";
import OpenSignUpDialog from "../../commands/OpenSignUpDialog";
import LoginByEmail from "../../../domain/auth/commands/LoginByEmail";
import UserLoggedIn from "../../../domain/auth/events/UserLoggedIn";
import LoginByEmailFailed from "../../../domain/auth/events/LoginByEmailFailed";
import LoginDialogDidClose from "../../../domain/auth/events/LoginDialogDidClose";
import SignUpDialogVM from "../../dialogs/sign-up-dialog/SignUpDialogVM";
import SignUpByEmail from "../../../domain/auth/commands/SignUpByEmail";
import UserSignedUpByEmail from "../../../domain/auth/events/UserSignedUpByEmail";
import FailedSignUpByEmail from "../../../domain/auth/events/FailedSignUpByEmail";
import SignUpDialogDidClose from "../../../domain/auth/events/SignUpDialogDidClose";
import SignUpByEmailData from "../../../domain/auth/interfaces/services/SignUpByEmailData";
import {AuthenticateByEmailData} from "../../../domain/auth/interfaces/services/AuthService";

class DialogsModule {

    public constructor(private readonly bus: Bus) {
    }

    public loginDialog = () => {
        return new LoginDialogVM(
            this.login,
            this.closeDialog,
            () => {
                this.bus.publish(new OpenSignUpDialog())
            }
        );
    }

    public signUpDialog = () => {
        return new SignUpDialogVM(
            this.signUp,
            this.closeDialog
        );
    }

    private login = async (data: AuthenticateByEmailData): Promise<void> => {

        const command = new LoginByEmail(data.email, data.password);
        const foundEvent = await this.bus.publishAndWaitFor(
            command,
            matchTypes(LoginDialogDidClose.TYPE, UserLoggedIn.TYPE, LoginByEmailFailed.TYPE)
        );

        switch (foundEvent.type) {
            case LoginDialogDidClose.TYPE:
                return;

            case UserLoggedIn.TYPE:
                return;

            case LoginByEmailFailed.TYPE:
                throw foundEvent.error;
        }
    }

    private signUp = async (data: SignUpByEmailData) => {

        const command = new SignUpByEmail(
            data.fullName,
            data.email,
            data.password,
        );

        const foundEvent = await this.bus.publishAndWaitFor(
            command,
            matchTypes(
                UserSignedUpByEmail.TYPE,
                FailedSignUpByEmail.TYPE,
                SignUpDialogDidClose.TYPE
            ),
        );

        switch (foundEvent.type) {
            case UserSignedUpByEmail.TYPE:
                return;

            case FailedSignUpByEmail.TYPE:
                throw foundEvent.error;

            case SignUpDialogDidClose.TYPE:
                return;
        }
    }

    private closeDialog = () => {
        debugger
        this.bus.publish(new CloseDialog());
    }
}

export default DialogsModule;