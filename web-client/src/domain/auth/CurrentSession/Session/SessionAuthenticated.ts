import UserInfo from "../../values/User";
import SessionStatus from "./SessionStatus";
import PersonalUserInfo from "./entities/PersonalUserInfo";

export default interface SessionAuthenticated {
    type: SessionStatus.AUTHENTICATED;

    logout(): Promise<void>;
    getAccessToken(): string;
    getUserInfo(): PersonalUserInfo;
}
