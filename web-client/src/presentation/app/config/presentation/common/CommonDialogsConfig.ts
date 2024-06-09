import LoginDialogVM from "../../../../dialogs/login-dialog/LoginDialogVM.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import CloseDialog from "../../../../commands/CloseDialog.ts";
import OpenSignUpDialog from "../../../../commands/OpenSignUpDialog.ts";
import LoginByEmail from "../../../../../domain/auth/commands/LoginByEmail.ts";
import UserLoggedIn from "../../../../../domain/auth/events/login/UserLoggedIn.ts";
import FailedLoginByEmail from "../../../../../domain/auth/events/login/login-by-email/FailedLoginByEmail.ts";
import LoginDialogDidClose from "../../../../../domain/auth/events/LoginDialogDidClose.ts";
import SignUpDialogVM from "../../../../dialogs/sign-up-dialog/SignUpDialogVM.ts";
import SignUpByEmail from "../../../../../domain/auth/commands/SignUpByEmail.ts";
import UserSignedUpByEmail from "../../../../../domain/auth/events/sign-up/UserSignedUpByEmail.ts";
import FailedSignUpByEmail from "../../../../../domain/auth/events/sign-up/FailedSignUpByEmail.ts";
import SignUpDialogDidClose from "../../../../../domain/auth/events/SignUpDialogDidClose.ts";
import OpenLoginDialog from "../../../../commands/OpenLoginDialog.ts";
import OpenedLoginDialog from "../../../../events/OpenedLoginDialog.ts";

class CommonDialogsConfig {

    public constructor(private readonly bus: Bus) {
    }

    public loginDialog = () => {

        const vm = new LoginDialogVM();

        vm.listenOwnEvents(this.bus, (event) => {
            if (event instanceof UserLoggedIn) {
                vm.displayDidLogin();
                return;
            }

            if (event instanceof FailedLoginByEmail) {
                vm.displayFailedLoginWrongCredentials();
                return;
            }

            if (event instanceof LoginDialogDidClose) {
                vm.displayDidClose();
                return;
            }
        });

        vm.delegate = {
            login: async (data) => {
                this.bus.publish(
                    new LoginByEmail(
                        data.email,
                        data.password
                    ).withSender(vm)
                );
            },
            openSignUpDialog: () => {
                this.bus.publish(new OpenSignUpDialog().withSender(vm));
            },
            closeDialog: () => {
                this.bus.publish(new CloseDialog().withSender(vm));
            }
        };

        return vm;
    }

    public signUpDialog = () => {
        const vm = new SignUpDialogVM();

        vm.listenOwnEvents(this.bus, (foundEvent) => {

            if (foundEvent instanceof UserSignedUpByEmail) {
                vm.displayDidSignUp();
                return;
            }

            if (foundEvent instanceof FailedSignUpByEmail) {

                if ((foundEvent.error as any)?.code === "UserWithSameEmailAlreadyExists") {
                    vm.displayFailedSignUpEmailAlreadyExists();
                    return;
                }

                vm.displayFailedSignUpSomethingWrong();
                return;
            }

            if (foundEvent instanceof SignUpDialogDidClose) {
                return;
            }

            if (foundEvent instanceof OpenedLoginDialog) {
                return;
            }
        });

        vm.delegate = {
            signUp: async (data) => {
                this.bus.publish(
                    new SignUpByEmail(
                        data.fullName,
                        data.email,
                        data.password,
                    ).withSender(vm)
                );
            },
            closeDialog: () => {
                this.bus.publish(new CloseDialog().withSender(vm));
            },
            openLoginDialog: () => {
                this.bus.publish(new OpenLoginDialog().withSender(vm));
            }
        };

        return vm;
    }
}

export default CommonDialogsConfig;