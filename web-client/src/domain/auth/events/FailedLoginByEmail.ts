import {ConnectionError, WrongCredentialsError} from "../interfaces/services/AuthService";
import AppEvent from "../../utils/AppEvent";

class FailedLoginByEmail extends AppEvent {

    public static readonly TYPE = "FailedLoginByEmail";

    constructor(public readonly error: WrongCredentialsError | ConnectionError) {
        super();
    }
}

export default FailedLoginByEmail;