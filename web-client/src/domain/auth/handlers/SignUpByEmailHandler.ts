import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import SignUpByEmail from "../commands/SignUpByEmail";
import UserSignedUpByEmail from "../events/UserSignedUpByEmail";
import Handler from "../../utils/Handler";
import FailedSignUpByEmail from "../events/FailedSignUpByEmail";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";

class SignUpByEmailHandler extends Handler {

    public constructor(
        private readonly auth: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: SignUpByEmail): Promise<void> => {
        try {
            const result = await this.auth.signUpByEmail(command);
            const userInfo = await this.userInfoService.getUserInfo(result.userId);
            this.bus.publish(new UserSignedUpByEmail(userInfo));
        } catch (error) {
            this.bus.publish(new FailedSignUpByEmail(error));
        }
    }
}

export default SignUpByEmailHandler;