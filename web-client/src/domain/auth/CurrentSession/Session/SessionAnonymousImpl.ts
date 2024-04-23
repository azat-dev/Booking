import Email from "../../values/Email";
import Password from "../../values/Password";
import SessionAnonymous from "./SessionAnonymous";
import SessionStatus from "./SessionStatus";
import TokensRepository from "../../interfaces/repositories/TokensRepository";
import AuthService from "./AuthService";
import { Session } from "./Session";
import SessionAuthenticatedImpl from "./SessionAuthenticatedImpl";

class SessionAnonymousImpl implements SessionAnonymous {
    public readonly type = SessionStatus.ANONYMOUS;

    public constructor(
        private authService: AuthService,
        private tokensRepository: TokensRepository,
        private setNext: (next: Session) => void
    ) {}

    public authenticate = async (
        email: Email,
        password: Password
    ): Promise<void> => {
        this.setNext({
            type: SessionStatus.PROCESSING,
        });

        try {
            const result = await this.authService.authenticateByEmail(
                email,
                password
            );

            this.setNext(
                new SessionAuthenticatedImpl(
                    result.accessToken,
                    result.userInfo,
                    this.authService,
                    this.tokensRepository,
                    this.setNext
                )
            );
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

            this.setNext(
                new SessionAuthenticatedImpl(
                    token,
                    user,
                    this.authService,
                    this.tokensRepository,
                    this.setNext
                )
            );
        } catch (error) {
            this.setNext(this);
            throw error;
        }
    };
}

export default SessionAnonymousImpl;
