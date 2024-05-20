import AppSession from "../entities/AppSession";
import Policy from "../../utils/Policy";

class UpdateAppSessionOnFailedLoginByToken extends Policy {

    public static readonly TYPE = "UpdateAppSessionOnFailedLoginByToken";

    public constructor(
        private readonly appSession: AppSession
    ) {
        super();
    }

    public execute = (): void => {
        this.appSession.logout();
    }
}

export default UpdateAppSessionOnFailedLoginByToken;