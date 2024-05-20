import AppEvent from "../../utils/AppEvent";

class SignUpDialogDidClose extends  AppEvent {

    public static readonly TYPE = "SignUpDialogDidClose";
}

export default SignUpDialogDidClose;