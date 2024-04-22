import Email from "../../values/Email";
import Password from "../../values/Password";
import User from "../../values/User";

export interface AuthenticationByEmailResult {
    readonly accessToken: string;
    readonly userInfo: User;
}

export default interface AuthService {
    authenticateByEmail(
        email: Email,
        password: Password
    ): Promise<AuthenticationByEmailResult>;

    authenticateByToken(token: string): Promise<User>;

    logout(): Promise<void>;
}
