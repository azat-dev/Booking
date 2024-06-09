import {ConnectionError, WrongCredentialsError} from "../../../interfaces/services/AuthService.ts";
import AppEvent from "../../../../utils/AppEvent.ts";

class FailedLoginByEmail extends AppEvent {

    constructor(
        public readonly error: WrongCredentialsError | ConnectionError
    ) {
        super();
    }
}

export default FailedLoginByEmail;