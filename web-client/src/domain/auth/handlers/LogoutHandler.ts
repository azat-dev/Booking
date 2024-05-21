import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import UserLoggedOut from "../events/UserLoggedOut";
import Handler from "../../utils/Handler";

class LogoutHandler extends Handler {

    public constructor(
        private readonly authService: AuthService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (): Promise<void> => {

        await this.authService.logout();
        this.bus.publish(new UserLoggedOut());
    }
}

export default LogoutHandler;