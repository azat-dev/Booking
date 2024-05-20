import Email from "../values/Email";
import AppEvent from "../../utils/AppEvent";

class SignedUpByEmail extends AppEvent {

    public static readonly TYPE = "SignedUpByEmail";

    constructor(public readonly email: Email) {
        super();
    }
}

export default SignedUpByEmail;