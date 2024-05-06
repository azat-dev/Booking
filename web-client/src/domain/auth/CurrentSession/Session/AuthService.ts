import Email from "../../values/Email";
import Password from "../../values/Password";
import UserInfo from "../../values/User";
import UserId from "../../values/UserId";
import SignUpByEmailData from "./SignUpByEmailData";

export interface Tokens {
    access: string;
    refresh: string;
}
export interface AuthenticationByEmailResult {
    readonly accessToken: string;
    readonly userId: UserId;
}

export interface SignUpByEmailResult {
    readonly tokens: Tokens;
    readonly userId: UserId;
}

export interface SignUpByEmail {
    (data: SignUpByEmailData): Promise<SignUpByEmailResult>;
}

export interface AuthenticateByEmailData {
    email: Email;
    password: Password;
}

export interface AuthenticateByEmail {
    (data: AuthenticateByEmailData): Promise<AuthenticationByEmailResult>;
}

export default interface AuthService {
    readonly authenticateByEmail: AuthenticateByEmail;
    readonly signUpByEmail: SignUpByEmail;

    authenticateByToken(token: string): Promise<UserInfo>;

    logout(): Promise<void>;
}
