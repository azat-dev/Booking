import {AuthenticateByEmailData} from "../../../domain/auth/CurrentSession/Session/AuthService";
import Email from "../../../domain/auth/values/Email";
import value from "../../utils/binding/value";
import FormInputViewModel from "../sign-up-dialog/form-input/FormInputViewModel";

class LoginDialogViewModel {
    public readonly isProcessing = value(false);
    public readonly showWrongCredentialsError = value(false);

    public readonly passwordInput: FormInputViewModel;
    public readonly emailInput: FormInputViewModel;

    private readonly inputs: FormInputViewModel[];

    public constructor(
        private login?: (data: AuthenticateByEmailData) => Promise<void>,
        private readonly onClose?: () => void,
        private readonly onSignUp?: () => void
    ) {

        this.emailInput = new FormInputViewModel(
            "",
            (value) => {
                if (this.isProcessing.value) {
                    return;
                }
                this.resetErrors();
                this.emailInput.updateValue(value);
            }
        )

        this.passwordInput = new FormInputViewModel(
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
        this.onClose?.();
    };

    public validateInput = () => {

        let isValid = true;

        const updateErrorTextFromException = (input: FormInputViewModel, e: any, ValidationException: any) => {
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
            await this.login?.({
                email,
                password,
            });
            return;

        } catch (e) {
            console.error(e);
            this.isProcessing.set(false);
            this.showWrongCredentialsError.set(true);
        }
    };

    public signUp = () => {
        this.onSignUp?.();
    };

    private resetErrors = () => {
        this.inputs.forEach(input => input.resetError());
    }
}

export default LoginDialogViewModel;
