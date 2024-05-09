import SessionAnonymous from "./SessionAnonymous";
import SessionStatus from "./SessionStatus";
import LocalAuthDataRepository from "../../interfaces/repositories/LocalAuthDataRepository";
import AuthService, {AuthenticateByEmailData} from "./AuthService";
import {Session} from "./Session";
import SessionAuthenticatedImpl from "./SessionAuthenticatedImpl";
import SignUpByEmailData from "./SignUpByEmailData";
import PersonalUserInfoService from "./PersonalUserInfoService";
import PersonalUserInfo from "./entities/PersonalUserInfo";

class SessionAnonymousImpl implements SessionAnonymous {
    public readonly type = SessionStatus.ANONYMOUS;

    public constructor(
        private authService: AuthService,
        private userInfoService: PersonalUserInfoService,
        private localAuthDataRepository: LocalAuthDataRepository,
        private setNext: (next: Session) => void
    ) {
    }

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

            await this.setAuthenticated(userInfo, result.tokens.access);
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

            await this.setAuthenticated(userInfo, signUpResult.tokens.access);
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
            const authData = await this.localAuthDataRepository.get();
            if (!authData) {
                this.setNext(this);
                return;
            }

            const token = authData.accessToken.val;

            const user = await this.authService.authenticateByToken(token);
            await this.setAuthenticated(user, token);

            // FXIME: Handle network errors
        } catch (error) {
            this.setNext(this);
            throw error;
        }
    };

    private logout = async (): Promise<void> => {
        await this.authService.logout();
        await this.localAuthDataRepository.clear();
        this.setNext(this);
    };

    private setAuthenticated = async (
        userInfo: PersonalUserInfo,
        accessToken: string
    ): Promise<void> => {
        this.setNext(
            new SessionAuthenticatedImpl(accessToken, userInfo, this.logout)
        );
    };
}

export default SessionAnonymousImpl;
