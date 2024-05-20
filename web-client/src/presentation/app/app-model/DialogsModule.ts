import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
import Bus from "../../../domain/utils/Bus";
import CloseDialog from "../../commands/CloseDialog";
import OpenSignUpDialog from "../../commands/OpenSignUpDialog";
import LoginByEmail from "../../../domain/auth/commands/LoginByEmail";
import UserLoggedIn from "../../../domain/auth/events/UserLoggedIn";
import LoginByEmailFailed from "../../../domain/auth/events/LoginByEmailFailed";
import LoginDialogDidClose from "../../../domain/auth/events/LoginDialogDidClose";

class DialogsModule {

    public constructor(
        private readonly bus: Bus
    ) {
        this.registerHandlers();
    }

    public loginDialog = () => {
        return new LoginDialogVM(
            async (data): Promise<void> => {

                await this.bus.publishAndWaitForResponse(
                    new LoginByEmail(data.email, data.password),
                    async (event) => {

                        switch (event.type) {
                            case LoginDialogDidClose.TYPE:
                                return;

                            case UserLoggedIn.TYPE:
                                return;

                            case LoginByEmailFailed:
                                throw event.error;
                        }
                    });
            },
            this.closeDialog,
            () => {
                this.bus.publish(new OpenSignUpDialog())
            }
        );
    }

    private registerHandlers = () => {
    }

    private closeDialog = () => {
        this.bus.publish(new CloseDialog());
    }
}

export default DialogsModule;