import LoginByToken from "../commands/LoginByToken";
import type Bus from "../../utils/Bus";
import type AuthService from "../interfaces/services/AuthService";
import UserLoggedIn from "../events/UserLoggedIn";
import FailedLoginByToken from "../events/FailedLoginByToken";
import Handler from "../../utils/Handler";


class LoginByTokenHandler extends Handler {

    public constructor(
        private readonly authService: AuthService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoginByToken): Promise<void> => {

        try {

            const user = await this.authService.authenticateByToken(command.token);
            this.bus.publish(new UserLoggedIn(user));
            // FXIME: Handle network errors
        } catch (error: any) {
            this.bus.publish(new FailedLoginByToken(error));
        }
    }
}

export default LoginByTokenHandler;