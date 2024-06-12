import Email from "../../values/Email";
import UserId from "../../values/UserId";
import SignUpByEmailData from "./SignUpByEmailData";
import PersonalUserInfo from "../../values/PersonalUserInfo";
import AccessToken from "../repositories/AccessToken";
import Password from "../../values/Password";

export interface Tokens {
    access: string;
    refresh: string;
}

export interface AuthenticationByEmailResult {
    readonly tokens: Tokens;
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
    password: string;
}

export class WrongCredentialsError extends Error {
    constructor() {
        super("Wrong credentials");
    }
}

export class ConnectionError extends Error {
    constructor() {
        super("Connection error");
    }
}

export default interface AuthService {

    readonly signUpByEmail: SignUpByEmail;

    /**
     * @throws WrongCredentialsError
     */
    authenticateByEmail(data: AuthenticateByEmailData): Promise<AuthenticationByEmailResult>;

    authenticateByToken(token: AccessToken): Promise<PersonalUserInfo>;

    logout(): Promise<void>;
}
