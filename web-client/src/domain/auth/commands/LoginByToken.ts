import AccessToken from "../interfaces/repositories/AccessToken";
import Command from "../../utils/Command";

class LoginByToken extends Command {

    public static readonly TYPE = 'LoginByToken';

    public constructor(public readonly token: AccessToken) {
        super();
    }
}

export default LoginByToken;