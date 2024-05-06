import Email from "../../values/Email";
import Password from "../../values/Password";
import FullName from "./FullName";

interface SignUpByEmailData {
    email: Email;
    password: Password;
    fullName: FullName;
}

export default SignUpByEmailData;
