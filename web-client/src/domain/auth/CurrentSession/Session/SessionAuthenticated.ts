import User from "../../values/User";
import SessionStatus from "./SessionStatus";

export default interface SessionAuthenticated {
    type: SessionStatus.AUTHENTICATED;
    logout(): Promise<void>;
    getAccessToken(): string;
    getUserInfo(): User;
}
