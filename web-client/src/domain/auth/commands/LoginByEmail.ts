import Email from "../values/Email";
import Command from "../../utils/Command";

class LoginByEmail extends Command {

    public constructor(
        public readonly email: Email,
        public readonly password: string
    ) {
        super();
    }
}

export default LoginByEmail;