import type AuthService from "../interfaces/services/AuthService";
import type Bus from "../../utils/Bus";
import SignUpByEmail from "../commands/SignUpByEmail";
import SignedUpByEmail from "../events/SignedUpByEmail";
import Handler from "../../utils/Handler";

class SignUpHandler extends Handler {

    public static readonly TYPE = "SignUpHandler";

    public constructor(
        private readonly auth: AuthService,
        private readonly bus: Bus
    ) {
        super();
    }

    public handle = async (command: SignUpByEmail): Promise<void> => {
        try {
            await this.auth.signUpByEmail(command);
            this.bus.publish(new SignedUpByEmail(command.email));
        } catch (error) {
            throw error;
        }
    }
}

export default SignUpHandler;