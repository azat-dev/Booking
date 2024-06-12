import type {Emit} from "../../utils/Bus";
import type LocalAuthDataRepository from "../interfaces/repositories/LocalAuthDataRepository";
import FailedLoginByToken from "../events/login/login-by-token/FailedLoginByToken.ts";
import Policy from "../../utils/Policy";
import AccessToken from "../interfaces/repositories/AccessToken.ts";


class WhenAppStartedThenTryLoginByToken extends Policy {

    public constructor(
        private readonly localAuthData: LocalAuthDataRepository,
        private readonly loginByToken: (token: AccessToken) => Promise<void>,
        private readonly emit: Emit
    ) {
        super();
    }

    public execute = async (): Promise<void> => {

        const data = await this.localAuthData.get();
        const token = data?.accessToken;

        if (!token) {
            this.emit(new FailedLoginByToken("No token found"))
            return;
        }

        try {
            await this.loginByToken(token);
        } catch (e) {
        }
    }
}

export default WhenAppStartedThenTryLoginByToken;