import AppEvent from "../../utils/AppEvent";

class LoginDialogDidClose extends AppEvent {

    public static readonly TYPE = "LoginDialogDidClose";

    public constructor() {
        super();
    }
}

export default LoginDialogDidClose;