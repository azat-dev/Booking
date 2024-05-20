import AppEvent from "../../domain/utils/AppEvent";

class OpenedSignUpDialog extends AppEvent {
    public static readonly TYPE = "OpenedSignUpDialog";

    public get type(): string {
        return OpenedSignUpDialog.TYPE;
    }
}

export default OpenedSignUpDialog;