import Email from "../../values/Email";
import Password from "../../values/Password";
import SessionAnonymous from "./SessionAnonymous";
import SessionStatus from "./SessionStatus";
import TokensRepository from "../../interfaces/repositories/TokensRepository";
import AuthService, { AuthenticateByEmailData } from "./AuthService";
import { Session } from "./Session";
import SessionAuthenticatedImpl from "./SessionAuthenticatedImpl";
import SignUpByEmailData from "./SignUpByEmailData";
import UserInfoService from "./UserInfoService";
import UserInfo from "../../values/User";

class SessionAnonymousImpl implements SessionAnonymous {
    public readonly type = SessionStatus.ANONYMOUS;

    public constructor(
        private authService: AuthService,
        private userInfoService: UserInfoService,
        private tokensRepository: TokensRepository,
        private setNext: (next: Session) => void
    ) {}

    private logout = async (): Promise<void> => {
        await this.authService.logout();
        await this.tokensRepository.clear();
        this.setNext(this);
    };

    private setAuthenticated = async (
        userInfo: UserInfo,
        accessToken: string
    ): Promise<void> => {
        this.setNext(
            new SessionAuthenticatedImpl(accessToken, userInfo, this.logout)
        );
    };

    public authenticate = async (
        data: AuthenticateByEmailData
    ): Promise<void> => {
        this.setNext({
            type: SessionStatus.PROCESSING,
        });

        try {
            const result = await this.authService.authenticateByEmail(data);

            const userInfo = await this.userInfoService.getUserInfo(
                result.userId
            );

            this.setAuthenticated(userInfo, result.accessToken);
        } catch (error) {
            this.setNext(this);
            throw error;
        }
    };

    public signUpByEmail = async (data: SignUpByEmailData): Promise<void> => {
        try {
            const signUpResult = await this.authService.signUpByEmail(data);
            const userInfo = await this.userInfoService.getUserInfo(
                signUpResult.userId
            );

            this.setAuthenticated(userInfo, signUpResult.tokens.access);
        } catch (error) {
            this.setNext(this);
            throw error;
        }
    };

    public tryToLoadLastSession = async (): Promise<void> => {
        this.setNext({
            type: SessionStatus.PROCESSING,
        });

        try {
            const token = await this.tokensRepository.getAccessToken();
            if (!token) {
                this.setNext(this);
                return;
            }

            const user = await this.authService.authenticateByToken(token);
            this.setAuthenticated(user, token);

            // FXIME: Handle network errors
        } catch (error) {
            this.setNext(this);
            throw error;
        }
    };
}

export default SessionAnonymousImpl;
