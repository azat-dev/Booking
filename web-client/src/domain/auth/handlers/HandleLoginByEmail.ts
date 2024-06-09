import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import FailedLoginByEmail from "../events/login/login-by-email/FailedLoginByEmail.ts";
import type PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import LoginByEmail from "../commands/LoginByEmail";
import UserLoggedIn from "../events/login/UserLoggedIn.ts";
import Handler from "../../utils/Handler";
import ProcessingLoginByEmail from "../events/login/login-by-email/ProcessingLoginByEmail.ts";

class HandleLoginByEmail extends Handler {

    public constructor(
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoginByEmail): Promise<void> => {

        try {
            this.bus.publish(new ProcessingLoginByEmail().withSender(command.senderId));

            const result = await this.authService.authenticateByEmail({
                email: command.email,
                password: command.password
            });

            const userInfo = await this.userInfoService.getUserInfo(
                result.userId
            );

            this.bus.publish(new UserLoggedIn(userInfo).withSender(command.senderId));

        } catch (error) {
            this.bus.publish(new FailedLoginByEmail(error as any).withSender(command.senderId));
        }
    }
}

export default HandleLoginByEmail;