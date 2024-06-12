import Email from "../../../domain/auth/values/Email";
import Password from "../../../domain/auth/values/Password";
import value from "../../utils/binding/value";
import FullName from "../../../domain/auth/values/FullName";
import FirstName from "../../../domain/auth/values/FirstName";
import LastName from "../../../domain/auth/values/LastName";
import FormInputVM from "./form-input/FormInputVM";
import VM from "../../utils/VM.ts";

class SignUpDialogVM extends VM {

    public isProcessing = value(false);
    public errorText = value<string | undefined>(undefined);

    public readonly firstNameInput: FormInputVM;
    public readonly lastNameInput: FormInputVM;
    public readonly emailInput: FormInputVM;
    public readonly passwordInput: FormInputVM;
    private readonly inputs: FormInputVM[];

    public constructor(
        private readonly signUp: (fullName: FullName, email: Email, password: Password) => Promise<void>,
        private readonly closeDialog: () => void,
        private readonly openLoginDialog: () => void,
    ) {
        super();
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
        this.closeDialog();
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
            await this.signUp(
                new FullName(
                    FirstName.checkAndCreate(this.firstNameInput.getValue() ?? ""),
                    LastName.checkAndCreate(this.lastNameInput.getValue() ?? "")
                ),
                Email.checkAndCreateFromString(this.emailInput.getValue() ?? ""),
                new Password(this.passwordInput.getValue() ?? "")
            );

            this.displayDidSignUp();
        } catch (e: any) {
            if ((e.error as any)?.code === "UserWithSameEmailAlreadyExists") {
                this.displayFailedSignUpEmailAlreadyExists();
                return
            }

            this.displayFailedSignUpSomethingWrong();
        }
    };

    private displayDidSignUp = () => {
        this.errorText.set(undefined);
        this.isProcessing.set(false);
        this.close();
    }

    public logIn = () => {
        this.openLoginDialog();
    };

    private displayFailedSignUpEmailAlreadyExists = () => {
        this.errorText.set("User with same email already exists.");
        this.isProcessing.set(false);
    }

    private displayFailedSignUpSomethingWrong = () => {
        this.errorText.set("Something went wrong. Please try again.");
        this.isProcessing.set(false);
    }
}

export default SignUpDialogVM;
