import Email from "../../../domain/auth/values/Email";
import Password from "../../../domain/auth/values/Password";
import value from "../../utils/binding/value";

class LoginDialogViewModel {
    public isProcessing = value(false);
    public showWrongCredentialsError = value(false);

    public constructor(
        private onLogin?: (email: Email, password: Password) => Promise<void>,
        private readonly onClose?: () => void
    ) {}

    public close = () => {
        this.onClose?.();
    };

    public submit = async () => {
        this.isProcessing.set(true);

        const success = await this.onLogin?.(
            new Email("email@some.com"),
            new Password("password")
        );
        if (success) {
            return;
        }

        this.isProcessing.set(false);
        this.showWrongCredentialsError.set(true);
    };
}

export default LoginDialogViewModel;
