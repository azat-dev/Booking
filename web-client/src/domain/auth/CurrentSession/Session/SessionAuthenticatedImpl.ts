import SessionAuthenticated from "./SessionAuthenticated";
import SessionStatus from "./SessionStatus";
import PersonalUserInfo from "./entities/PersonalUserInfo";

class SessionAuthenticatedImpl implements SessionAuthenticated {
    public readonly type = SessionStatus.AUTHENTICATED;

    public constructor(
        private accessToken: string,
        private userInfo: PersonalUserInfo,
        private onLogout: () => void
    ) {
    }

    public logout = async (): Promise<void> => {
        this.onLogout();
    };

    public getAccessToken = (): string => {
        return this.accessToken;
    };

    public getUserInfo = (): PersonalUserInfo => {
        return this.userInfo;
    };
}

export default SessionAuthenticatedImpl;
