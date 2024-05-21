import AccessToken from "../interfaces/repositories/AccessToken";
import Command from "../../utils/Command";

class LoginByToken extends Command {

    public constructor(public readonly token: AccessToken) {
        super();
    }
}

export default LoginByToken;