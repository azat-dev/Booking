import AppEvent from "../../utils/AppEvent";

class LoginByEmailFailed extends AppEvent {

    public static readonly TYPE = "LoginByEmailFailed";

    public constructor(public readonly error: any) {
        super();
    }
}

export default LoginByEmailFailed;