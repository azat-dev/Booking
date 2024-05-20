import AppEvent from "../../utils/AppEvent";

class FailedLoginByToken extends AppEvent {
    public static readonly TYPE = "FAILED_LOGIN_BY_TOKEN";

    public constructor(
        public readonly error: any
    ) {
        super();
    }
}

export default FailedLoginByToken;