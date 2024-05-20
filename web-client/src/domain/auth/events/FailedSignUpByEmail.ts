import {ConnectionError, WrongCredentialsError} from "../interfaces/services/AuthService";
import AppEvent from "../../utils/AppEvent";

class FailedSignUpByEmail extends AppEvent {

    public static readonly TYPE = "FAILED_SIGN_UP_BY_EMAIL";

    constructor(public readonly error: any) {
        super();
    }
}

export default FailedSignUpByEmail;