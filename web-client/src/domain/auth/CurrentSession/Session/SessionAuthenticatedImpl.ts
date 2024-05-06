import SessionAuthenticated from "./SessionAuthenticated";
import SessionStatus from "./SessionStatus";
import UserInfo from "../../values/User";

class SessionAuthenticatedImpl implements SessionAuthenticated {
    public readonly type = SessionStatus.AUTHENTICATED;

    public constructor(
        private accessToken: string,
        private userInfo: UserInfo,
        private onLogout: () => void
    ) {}

    public logout = async (): Promise<void> => {
        this.onLogout();
    };

    public getAccessToken = (): string => {
        return this.accessToken;
    };

    public getUserInfo = (): UserInfo => {
        return this.userInfo;
    };
}

export default SessionAuthenticatedImpl;
