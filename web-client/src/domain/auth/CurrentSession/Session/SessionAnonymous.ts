import { AuthenticateByEmailData } from "./AuthService";
import SessionStatus from "./SessionStatus";
import SignUpByEmailData from "./SignUpByEmailData";

export default interface SessionAnonymous {
    type: SessionStatus.ANONYMOUS;

    tryToLoadLastSession(): Promise<void>;

    authenticate(data: AuthenticateByEmailData): Promise<void>;

    signUpByEmail(data: SignUpByEmailData): Promise<void>;
}
