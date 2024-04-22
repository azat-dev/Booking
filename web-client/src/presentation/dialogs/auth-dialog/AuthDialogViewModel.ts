import value from "../../utils/binding/value";

class AuthDialogViewModel {
    public isOpen = value(false);
    public isProcessing = value(false);
    public showWrongCredentialsError = value(false);

    public constructor(
        private onLogin?: (email: string, password: string) => Promise<boolean>,
        private readonly onClose?: () => void
    ) {}

    public close = () => {
        this.isOpen.set(false);
    };

    public didClose = () => {
        this.onClose?.();
    };

    public submit = async () => {
        this.isProcessing.set(true);

        const success = await this.onLogin?.("email", "password");
        if (success) {
            return;
        }

        this.isProcessing.set(false);
        this.showWrongCredentialsError.set(true);
    };

    public didMount = () => {
        this.isOpen.set(true);
    };
}

export default AuthDialogViewModel;
