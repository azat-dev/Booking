import AppEvent from "../../domain/utils/AppEvent";

class OpenedLoginDialog  extends AppEvent {
    public static readonly TYPE = "OpenedLoginDialog";

    public get type(): string {
        return OpenedLoginDialog.TYPE;
    }
}

export default OpenedLoginDialog;