import AppEvent from "../../utils/AppEvent";

class FailedAuthenticateByToken extends AppEvent {

    public static readonly TYPE = "FailedAuthenticateByToken";
}

export default FailedAuthenticateByToken;