import type Bus from "../../utils/Bus";
import type LocalAuthDataRepository from "../interfaces/repositories/LocalAuthDataRepository";
import LoginByToken from "../commands/LoginByToken";
import FailedLoginByToken from "../events/FailedLoginByToken";
import Policy from "../../utils/Policy";


class WhenAppStartedThenTryLoginByToken extends Policy {

    public constructor(
        private readonly localAuthData: LocalAuthDataRepository,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (): Promise<void> => {

        const data = await this.localAuthData.get();
        const token = data?.accessToken;

        if (!token) {
            this.bus.publish(new FailedLoginByToken("No token found"))
            return;
        }

        this.bus.publish(new LoginByToken(token));
    }
}

export default WhenAppStartedThenTryLoginByToken;