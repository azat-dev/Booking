import AppEvent from "../../utils/AppEvent";

class LoginByEmailFailed extends AppEvent {

    public static readonly TYPE = "LOGIN_BY_EMAIL_FAILED";

    public constructor(public readonly error: any) {
        super();
    }
}

export default LoginByEmailFailed;