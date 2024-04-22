import Email from "../../values/Email";
import Password from "../../values/Password";

export default interface AuthenticateByEmail {
    (email: Email, password: Password): Promise<void>;
}
