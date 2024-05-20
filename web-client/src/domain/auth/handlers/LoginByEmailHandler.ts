import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import LoginByEmailFailed from "../events/LoginByEmailFailed";
import type PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import LoginByEmail from "../commands/LoginByEmail";
import UserLoggedIn from "../events/UserLoggedIn";
import Handler from "../../utils/Handler";

class LoginByEmailHandler extends Handler {

    public static readonly TYPE = "LOGIN_BY_EMAIL_HANDLER";

    public constructor(
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoginByEmail): Promise<void> => {

        try {
            const result = await this.authService.authenticateByEmail({
                email: command.email,
                password: command.password
            });

            const userInfo = await this.userInfoService.getUserInfo(
                result.userId
            );

            this.bus.publish(new UserLoggedIn(userInfo));

        } catch (error) {
            this.bus.publish(new LoginByEmailFailed(error));
        }
    }
}

export default LoginByEmailHandler;