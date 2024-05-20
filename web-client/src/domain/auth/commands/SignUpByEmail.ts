import Email from "../values/Email";
import Password from "../values/Password";
import FullName from "../values/FullName";
import Command from "../../utils/Command";

class SignUpByEmail extends Command {

    public static readonly TYPE = 'SIGNUP_BY_EMAIL';

    public constructor(
        public readonly fullName: FullName,
        public readonly email: Email,
        public readonly password: Password
    ) {
        super();
    }
}

export default SignUpByEmail;