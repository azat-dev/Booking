import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
import Bus, {matchClasses, matchTypes} from "../../../domain/utils/Bus";
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

class DialogsConfig {

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
            matchClasses(LoginDialogDidClose, UserLoggedIn, LoginByEmailFailed)
        );

        switch (foundEvent.constructor) {
            case LoginDialogDidClose:
                return;

            case UserLoggedIn:
                return;

            case LoginByEmailFailed:
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
            matchClasses(
                UserSignedUpByEmail,
                FailedSignUpByEmail,
                SignUpDialogDidClose
            ),
        );

        switch (foundEvent.constructor) {
            case UserSignedUpByEmail:
                return;

            case FailedSignUpByEmail:
                throw foundEvent.error;

            case SignUpDialogDidClose:
                return;
        }
    }

    private closeDialog = () => {
        this.bus.publish(new CloseDialog());
    }
}

export default DialogsConfig;