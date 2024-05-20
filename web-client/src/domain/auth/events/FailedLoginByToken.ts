import AppEvent from "../../utils/AppEvent";

class FailedLoginByToken extends AppEvent {
    public static readonly TYPE = "FailedLoginByToken";

    public constructor(
        public readonly error: any
    ) {
        super();
    }
}

export default FailedLoginByToken;