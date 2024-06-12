import type AuthService from "../interfaces/services/AuthService";
import UserLoggedOut from "../events/UserLoggedOut";
import {Emit} from "../../utils/Bus.ts";

class Logout {

    public constructor(
        private readonly authService: AuthService,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<void> => {

        await this.authService.logout();
        this.emit(new UserLoggedOut());
    }
}

export default Logout;