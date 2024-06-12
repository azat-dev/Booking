import Email from "../../../domain/auth/values/Email";
import value from "../../utils/binding/value";
import FormInputVM from "../sign-up-dialog/form-input/FormInputVM";
import VM from "../../utils/VM.ts";
import {WrongCredentialsError} from "../../../domain/auth/interfaces/services/AuthService.ts";

class LoginDialogVM extends VM {

    public readonly isProcessing = value(false);
    public readonly showWrongCredentialsError = value(false);

    public readonly passwordInput: FormInputVM;
    public readonly emailInput: FormInputVM;
    private readonly inputs: FormInputVM[];

    public constructor(
        private readonly login: (email: Email, password: string) => Promise<void>,
        private readonly openSignUpDialog: () => void,
        private readonly closeDialog: () => void,
    ) {
        super();
        this.emailInput = new FormInputVM(
            "",
            (value) => {
                if (this.isProcessing.value) {
                    return;
                }
                this.resetErrors();
                this.emailInput.updateValue(value);
            }
        )

        this.passwordInput = new FormInputVM(
            "",
            (value) => {
                if (this.isProcessing.value) {
                    return;
                }
                this.resetErrors();
                this.passwordInput.updateValue(value);
            }
        )

        this.inputs = [
            this.emailInput,
            this.passwordInput
        ];
    }

    public close = () => {
        this.closeDialog();
    };

    public validateInput = () => {

        let isValid = true;

        const updateErrorTextFromException = (input: FormInputVM, e: any, ValidationException: any) => {
            input.updateErrorText(e instanceof ValidationException ? e.message : undefined);
            input.updateIsWrong(true);
        }

        try {
            Email.checkAndCreateFromString(this.emailInput.getValue() ?? "");
            this.emailInput.resetError();
        } catch (e) {
            isValid = false;
            updateErrorTextFromException(this.emailInput, e, Email.ValidationException);
        }

        const password = this.passwordInput.getValue();

        if (password) {
            this.passwordInput.resetError();
        } else {
            isValid = false;
            this.passwordInput.updateErrorText("Password is required");
            this.passwordInput.updateIsWrong(true);
        }

        if (!isValid) {
            throw new Error("Invalid input")
        }
    }

    public submit = async () => {
        this.isProcessing.set(true);

        try {
            this.validateInput();
        } catch (e) {
            this.isProcessing.set(false);
            return;
        }

        const email = Email.checkAndCreateFromString(this.emailInput.getValue() ?? "");
        const password = this.passwordInput.getValue() ?? "";

        try {
            await this.login(
                email,
                password,
            );

            this.displayDidLogin();
        } catch (e) {

            if (e instanceof WrongCredentialsError) {
                this.displayFailedLoginWrongCredentials();
            }
        }
    };

    private displayDidLogin = () => {
        this.close();
    }

    private displayFailedLoginWrongCredentials = () => {
        this.isProcessing.set(false);
        this.showWrongCredentialsError.set(true);
    }

    public signUp = () => {
        this.openSignUpDialog();
    };

    private resetErrors = () => {
        this.inputs.forEach(input => input.resetError());
    }
}

export default LoginDialogVM;
