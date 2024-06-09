import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import SignUpByEmail from "../commands/SignUpByEmail";
import UserSignedUpByEmail from "../events/sign-up/UserSignedUpByEmail.ts";
import Handler from "../../utils/Handler";
import FailedSignUpByEmail from "../events/sign-up/FailedSignUpByEmail.ts";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import ProcessingSignUpByEmail from "../events/sign-up/ProcessingSignUpByEmail.ts";

class HandleSignUpByEmail extends Handler {

    public constructor(
        private readonly auth: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: SignUpByEmail): Promise<void> => {
        try {
            this.bus.publish(new ProcessingSignUpByEmail().withSender(command.senderId));
            const result = await this.auth.signUpByEmail(command);
            const userInfo = await this.userInfoService.getUserInfo(result.userId);
            this.bus.publish(new UserSignedUpByEmail(userInfo).withSender(command.senderId));
        } catch (error) {
            this.bus.publish(new FailedSignUpByEmail(error).withSender(command.senderId));
        }
    }
}

export default HandleSignUpByEmail;