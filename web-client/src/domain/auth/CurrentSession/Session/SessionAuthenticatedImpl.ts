import SessionAuthenticated from "./SessionAuthenticated";
import AuthService from "./AuthService";
import { Session } from "./Session";
import SessionAnonymousImpl from "./SessionAnonymousImpl";
import SessionStatus from "./SessionStatus";
import TokensRepository from "../../interfaces/repositories/TokensRepository";
import User from "../../values/User";

class SessionAuthenticatedImpl implements SessionAuthenticated {
    public readonly type = SessionStatus.AUTHENTICATED;

    public constructor(
        private accessToken: string,
        private userInfo: User,
        private authService: AuthService,
        private tokensRepository: TokensRepository,
        private setNext: (next: Session) => void
    ) {}

    public logout = async (): Promise<void> => {
        await this.authService.logout();
        await this.tokensRepository.clear();

        this.setNext(
            new SessionAnonymousImpl(
                this.authService,
                this.tokensRepository,
                this.setNext
            )
        );
    };

    public getAccessToken = (): string => {
        return this.accessToken;
    };

    public getUserInfo = (): User => {
        return this.userInfo;
    };
}

export default SessionAuthenticatedImpl;
