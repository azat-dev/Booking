import {ConnectionError, WrongCredentialsError} from "../interfaces/services/AuthService";
import AppEvent from "../../utils/AppEvent";

class FailedSignUpByEmail extends AppEvent {

    public static readonly TYPE = "FailedSignUpByEmail";

    constructor(public readonly error: any) {
        super();
    }
}

export default FailedSignUpByEmail;