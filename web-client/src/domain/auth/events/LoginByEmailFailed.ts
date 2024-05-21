import AppEvent from "../../utils/AppEvent";

class LoginByEmailFailed extends AppEvent {
    public constructor(public readonly error: any) {
        super();
    }
}

export default LoginByEmailFailed;