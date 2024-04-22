import Email from "../../values/Email";
import Password from "../../values/Password";
import SessionStatus from "./SessionStatus";

export default interface SessionAnonymous {
    type: SessionStatus.ANONYMOUS;

    tryToLoadLastSession(): Promise<void>;
    authenticate(email: Email, password: Password): Promise<void>;
}
