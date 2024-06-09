import LoginByToken from "../commands/LoginByToken";
import type Bus from "../../utils/Bus";
import type AuthService from "../interfaces/services/AuthService";
import UserLoggedIn from "../events/login/UserLoggedIn.ts";
import FailedLoginByToken from "../events/login/login-by-token/FailedLoginByToken.ts";
import Handler from "../../utils/Handler";
import ProcessingLoginByToken from "../events/login/login-by-token/ProcessingLoginByToken.ts";


class HandleLoginByToken extends Handler {

    public constructor(
        private readonly authService: AuthService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoginByToken): Promise<void> => {

        try {

            this.bus.publish(new ProcessingLoginByToken().withSender(command.senderId));
            const user = await this.authService.authenticateByToken(command.token);
            this.bus.publish(new UserLoggedIn(user).withSender(command.senderId));
            // FXIME: Handle network errors
        } catch (error: any) {
            this.bus.publish(new FailedLoginByToken(error).withSender(command.senderId));
        }
    }
}

export default HandleLoginByToken;