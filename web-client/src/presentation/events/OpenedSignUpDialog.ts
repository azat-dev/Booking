import AppEvent from "../../domain/utils/AppEvent";

class OpenedSignUpDialog extends AppEvent {
    public static readonly TYPE = "OPENED_SIGN_UP_DIALOG";

    public get type(): string {
        return OpenedSignUpDialog.TYPE;
    }
}

export default OpenedSignUpDialog;