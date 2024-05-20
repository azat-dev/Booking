import AppEvent from "../../domain/utils/AppEvent";

class OpenedLoginDialog  extends AppEvent {
    public static readonly TYPE = "OPENED_LOGIN_DIALOG";

    public get type(): string {
        return OpenedLoginDialog.TYPE;
    }
}

export default OpenedLoginDialog;