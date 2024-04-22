import Email from "./values/Email";
import Password from "./values/Password";

export enum SessionStatus {
    NOT_AUTHENTICATED = "not-authenticated",
    AUTH_PROCESSING = "auth-processing",
    AUTHENTICATED = "authenticated",
}

export interface SessionStateAuthProcessing {
    type: SessionStatus.AUTH_PROCESSING;
}

export interface SessionStateAuthenticated {
    type: SessionStatus.AUTHENTICATED;
    logout(): Promise<void>;
}

export interface SessionStateNotAuthenticated {
    type: SessionStatus.NOT_AUTHENTICATED;
    authenticate(email: Email, password: Password): Promise<void>;
}

export type SessionState =
    | SessionStateNotAuthenticated
    | SessionStateAuthProcessing
    | SessionStateAuthenticated;

export default interface Session {
    readonly state: SessionState;
}
