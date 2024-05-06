import UserInfo from "../../values/User";
import SessionStatus from "./SessionStatus";

export default interface SessionAuthenticated {
    type: SessionStatus.AUTHENTICATED;

    getUserInfo(): UserInfo;

    logout(): Promise<void>;
    getAccessToken(): string;
    getUserInfo(): UserInfo;
}
