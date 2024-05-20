import AppEvent from "../../utils/AppEvent";

class FailedAuthenticateByToken extends AppEvent {

    public static readonly TYPE = "FAILED_AUTHENTICATE_BY_TOKEN";
}

export default FailedAuthenticateByToken;