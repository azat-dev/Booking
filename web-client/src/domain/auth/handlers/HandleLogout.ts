import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import UserLoggedOut from "../events/UserLoggedOut";
import Handler from "../../utils/Handler";
import Logout from "../commands/Logout.ts";

class HandleLogout extends Handler {

    public constructor(
        private readonly authService: AuthService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: Logout): Promise<void> => {

        await this.authService.logout();
        this.bus.publish(new UserLoggedOut().withSender(command.senderId));
    }
}

export default HandleLogout;