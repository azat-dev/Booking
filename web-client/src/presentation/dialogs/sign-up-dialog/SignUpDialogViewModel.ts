import Email from "../../../domain/auth/values/Email";
import Password from "../../../domain/auth/values/Password";
import value from "../../utils/binding/value";
import FullName from "../../../domain/auth/CurrentSession/Session/FullName";
import FirstName from "../../../domain/auth/CurrentSession/Session/FirstName";
import LastName from "../../../domain/auth/CurrentSession/Session/LastName";
import FormInputViewModel from "./form-input/FormInputViewModel";
import SignUpByEmailData from "../../../domain/auth/CurrentSession/Session/SignUpByEmailData";

class SignUpDialogViewModel {
    public isProcessing = value(false);
    public errorText = value<string | undefined>(undefined);

    public readonly firstNameInput: FormInputViewModel;
    public readonly lastNameInput: FormInputViewModel;
    public readonly emailInput: FormInputViewModel;
    public readonly passwordInput: FormInputViewModel;

    private readonly inputs: FormInputViewModel[];

    public constructor(
        private signUp?: (data: SignUpByEmailData) => Promise<void>,
        private readonly onClose?: () => void,
        private readonly onOpenLoginDialog?: () => void
    ) {
        this.firstNameInput = new FormInputViewModel("", (newValue) => {
            this.resetErrors();
            this.firstNameInput.updateValue(newValue);
        });
        this.lastNameInput = new FormInputViewModel("", (newValue) => {
            this.resetErrors();
            this.lastNameInput.updateValue(newValue);
        });
        this.emailInput = new FormInputViewModel("", (newValue) => {
            this.resetErrors();
            this.emailInput.updateValue(newValue);
        });
        this.passwordInput = new FormInputViewModel("", (newValue) => {
            this.resetErrors();
            this.passwordInput.updateValue(newValue);
        });

        this.inputs = [
            this.firstNameInput,
            this.lastNameInput,
            this.emailInput,
            this.passwordInput,
        ];

        this.isProcessing.listen((isProcessing) => {
            this.inputs.forEach((input) =>
                input.updateIsDisabled(isProcessing)
            );
        });
    }

    public close = () => {
        this.onClose?.();
    };

    public resetErrors = () => {
        this.inputs.forEach((input) => {
            input.updateIsWrong(false);
            input.updateErrorText(undefined);
        });
    };

    public validateInput = () => {
        this.resetErrors();

        let hasErrors = false;

        try {
            new Email(this.emailInput.getValue() ?? "");
        } catch (e) {
            if (e instanceof Email.ValidationException) {
                this.emailInput.updateErrorText(e.message);
            } else {
                this.emailInput.updateErrorText(undefined);
            }

            this.emailInput.updateIsWrong(true);
            hasErrors = true;
        }

        try {
            new FirstName(this.firstNameInput.getValue() ?? "");
        } catch (e) {
            if (e instanceof FirstName.ValidationException) {
                this.firstNameInput.updateErrorText(e.message);
            } else {
                this.firstNameInput.updateErrorText(undefined);
            }
            this.firstNameInput.updateIsWrong(true);
            hasErrors = true;
        }

        try {
            new LastName(this.lastNameInput.getValue() ?? "");
        } catch (e) {
            if (e instanceof LastName.ValidationException) {
                this.lastNameInput.updateErrorText(e.message);
            } else {
                this.lastNameInput.updateErrorText(undefined);
            }

            this.lastNameInput.updateIsWrong(true);
            hasErrors = true;
        }

        try {
            new Password(this.passwordInput.getValue() ?? "");
        } catch (e) {
            if (e instanceof Password.ValidationException) {
                this.passwordInput.updateErrorText(e.message);
            } else {
                this.passwordInput.updateErrorText(undefined);
            }

            this.passwordInput.updateIsWrong(true);
            hasErrors = true;
        }

        return !hasErrors;
    };

    public submit = async () => {
        this.isProcessing.set(true);
        this.errorText.set(undefined);

        if (!this.validateInput()) {
            this.isProcessing.set(false);
            return;
        }

        try {
            const data = {
                fullName: new FullName(
                    new FirstName(this.firstNameInput.getValue() ?? ""),
                    new LastName(this.lastNameInput.getValue() ?? "")
                ),
                email: new Email(this.emailInput.getValue() ?? ""),
                password: new Password(this.passwordInput.getValue() ?? ""),
            };

            await this.signUp?.(data);
            this.isProcessing.set(false);
            return;
        } catch (e) {
            console.log("Error", e);
            if ((e as any)?.code === "UserAlreadyExists") {
                this.errorText.set("User with this email already exists.");
                this.isProcessing.set(false);
            } else {
                this.errorText.set("Something went wrong. Please try again.");
                this.isProcessing.set(false);
            }
        }
    };

    public logIn = () => {
        this.onOpenLoginDialog?.();
    };
}

export default SignUpDialogViewModel;
