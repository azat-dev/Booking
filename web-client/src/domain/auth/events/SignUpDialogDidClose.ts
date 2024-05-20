import AppEvent from "../../utils/AppEvent";

class SignUpDialogDidClose extends  AppEvent {

    public static readonly TYPE = "SIGN_UP_DIALOG_DID_CLOSE";
}

export default SignUpDialogDidClose;