import type {Emit} from "../../utils/Bus";
import type AuthService from "../interfaces/services/AuthService";
import UserLoggedIn from "../events/login/UserLoggedIn.ts";
import FailedLoginByToken from "../events/login/login-by-token/FailedLoginByToken.ts";
import ProcessingLoginByToken from "../events/login/login-by-token/ProcessingLoginByToken.ts";
import AccessToken from "../interfaces/repositories/AccessToken.ts";


class LoginByToken {

    public constructor(
        private readonly authService: AuthService,
        private readonly emit: Emit
    ) {
    }

    public execute = async (token: AccessToken): Promise<void> => {

        try {

            this.emit(new ProcessingLoginByToken());
            const user = await this.authService.authenticateByToken(token);
            this.emit(new UserLoggedIn(user));
            // FXIME: Handle network errors
        } catch (e: any) {
            const error = new FailedLoginByToken(e);
            this.emit(error);
            throw error;
        }
    }
}

export default LoginByToken;