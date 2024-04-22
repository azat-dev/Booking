import Email from "../../values/Email";
import Password from "../../values/Password";
import SessionAnonymous from "./SessionAnonymous";
import SessionStatus from "./SessionStatus";
import TokensRepository from "../../interfaces/repositories/TokensRepository";
import AuthService from "./AuthService";
import { Session } from "./Session";
import SessionAuthenticatedImpl from "./SessionAuthenticatedImpl";

class SessionAnonymousImpl implements SessionAnonymous {
    public readonly type = SessionStatus.NOT_AUTHENTICATED;

    public constructor(
        private authService: AuthService,
        private tokensRepository: TokensRepository,
        private setNext: (next: Session) => void
    ) {}

    public authenticate = async (
        email: Email,
        password: Password
    ): Promise<void> => {
        await this.authService.authenticateByEmail(email, password);
    };

    public tryToLoadLastSession = async (): Promise<void> => {
        const token = await this.tokensRepository.getAccessToken();
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
    };
}

export default SessionAnonymousImpl;
