import type AuthService from "../interfaces/services/AuthService";
import type {Emit} from "../../utils/Bus";
import UserSignedUpByEmail from "../events/sign-up/UserSignedUpByEmail.ts";
import FailedSignUpByEmail from "../events/sign-up/FailedSignUpByEmail.ts";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import ProcessingSignUpByEmail from "../events/sign-up/ProcessingSignUpByEmail.ts";
import FullName from "../values/FullName.ts";
import Email from "../values/Email.ts";
import Password from "../values/Password.ts";

class SignUpByEmail {

    public constructor(
        private readonly auth: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly emit: Emit
    ) {
    }

    public execute = async (
        fullName: FullName,
        email: Email,
        password: Password
    ): Promise<void> => {
        try {
            this.emit(new ProcessingSignUpByEmail());
            const result = await this.auth.signUpByEmail(
                {
                    fullName,
                    email,
                    password
                }
            );
            const userInfo = await this.userInfoService.getUserInfo(result.userId);
            this.emit(new UserSignedUpByEmail(userInfo));
        } catch (e) {
            const error = new FailedSignUpByEmail(e);
            this.emit(error);
            throw error;
        }
    }
}

export default SignUpByEmail;