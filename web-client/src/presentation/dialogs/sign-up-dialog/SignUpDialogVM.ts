import Email from "../../../domain/auth/values/Email";
import Password from "../../../domain/auth/values/Password";
import value from "../../utils/binding/value";
import FullName from "../../../domain/auth/values/FullName";
import FirstName from "../../../domain/auth/values/FirstName";
import LastName from "../../../domain/auth/values/LastName";
import FormInputVM from "./form-input/FormInputVM";
import SignUpByEmailData from "../../../domain/auth/interfaces/services/SignUpByEmailData";

class SignUpDialogVM {

    public static readonly TYPE = "SignUpDialogVM";
    public get type() {
        return SignUpDialogVM.TYPE;
    }

    public isProcessing = value(false);
    public errorText = value<string | undefined>(undefined);

    public readonly firstNameInput: FormInputVM;
    public readonly lastNameInput: FormInputVM;
    public readonly emailInput: FormInputVM;
    public readonly passwordInput: FormInputVM;

    private readonly inputs: FormInputVM[];

    public constructor(
        private signUp?: (data: SignUpByEmailData) => Promise<void>,
        private readonly onClose?: () => void,
        private readonly onOpenLoginDialog?: () => void
    ) {
        this.firstNameInput = new FormInputVM("", (newValue) => {
            this.resetErrors();
            this.firstNameInput.updateValue(newValue);
        });
        this.lastNameInput = new FormInputVM("", (newValue) => {
            this.resetErrors();
            this.lastNameInput.updateValue(newValue);
        });
        this.emailInput = new FormInputVM("", (newValue) => {
            this.resetErrors();
            this.emailInput.updateValue(newValue);
        });
        this.passwordInput = new FormInputVM("", (newValue) => {
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
        this.inputs.forEach((input) => input.resetError());
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
            updateErrorTextFromException(this.emailInput, e, Email.ValidationException);
            isValid = false;
        }

        try {
            FirstName.checkAndCreate(this.firstNameInput.getValue() ?? "");
            this.firstNameInput.resetError();
        } catch (e) {
            updateErrorTextFromException(this.firstNameInput, e, FirstName.ValidationException);
            isValid = false;
        }

        try {
            LastName.checkAndCreate(this.lastNameInput.getValue() ?? "");
            this.lastNameInput.resetError();
        } catch (e) {
            updateErrorTextFromException(this.lastNameInput, e, LastName.ValidationException);
            isValid = false;
        }

        try {
            new Password(this.passwordInput.getValue() ?? "");
            this.passwordInput.resetError();
        } catch (e) {
            updateErrorTextFromException(this.passwordInput, e, Password.ValidationException);
            isValid = false;
        }

        return isValid;
    };

    public submit = async () => {
        this.isProcessing.set(true);

        if (!this.validateInput()) {
            this.isProcessing.set(false);
            return;
        }

        try {
            const data = {
                fullName: new FullName(
                    FirstName.checkAndCreate(this.firstNameInput.getValue() ?? ""),
                    LastName.checkAndCreate(this.lastNameInput.getValue() ?? "")
                ),
                email: Email.checkAndCreateFromString(this.emailInput.getValue() ?? ""),
                password: new Password(this.passwordInput.getValue() ?? ""),
            };

            await this.signUp?.(data);
            this.errorText.set(undefined);
            this.isProcessing.set(false);
            return;
        } catch (e) {
            console.log("Error", e);
            if ((e as any)?.code === "UserWithSameEmailAlreadyExists") {
                this.errorText.set("User with same email already exists.");
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

export default SignUpDialogVM;
