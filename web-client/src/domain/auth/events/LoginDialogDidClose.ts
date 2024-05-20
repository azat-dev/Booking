import AppEvent from "../../utils/AppEvent";

class LoginDialogDidClose extends AppEvent {

    public static readonly TYPE = "LOGIN_DIALOG_DID_CLOSE";

    public constructor() {
        super();
    }
}

export default LoginDialogDidClose;