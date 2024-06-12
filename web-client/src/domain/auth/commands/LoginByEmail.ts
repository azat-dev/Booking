import type AuthService from "../interfaces/services/AuthService";
import type {Emit} from "../../utils/Bus";
import FailedLoginByEmail from "../events/login/login-by-email/FailedLoginByEmail.ts";
import type PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import UserLoggedIn from "../events/login/UserLoggedIn.ts";
import ProcessingLoginByEmail from "../events/login/login-by-email/ProcessingLoginByEmail.ts";
import Email from "../values/Email.ts";

class LoginByEmail {

    public constructor(
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly emit: Emit
    ) {
    }

    /**
     * throws WrongCredentialsError
     * @param email
     * @param password
     */
    public execute = async (email: Email, password: string): Promise<void> => {

        try {
            this.emit(new ProcessingLoginByEmail());

            const result = await this.authService.authenticateByEmail({
                email,
                password
            });

            const userInfo = await this.userInfoService.getUserInfo(
                result.userId
            );

            this.emit(new UserLoggedIn(userInfo));

        } catch (error) {
            this.emit(new FailedLoginByEmail(error as any));
            throw error;
        }
    }
}

export default LoginByEmail;