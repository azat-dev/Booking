import AccessToken from "../interfaces/repositories/AccessToken";
import Command from "../../utils/Command";

class LoginByToken extends Command {

    public static readonly TYPE = 'LOGIN_BY_TOKEN';

    public constructor(public readonly token: AccessToken) {
        super();
    }
}

export default LoginByToken;