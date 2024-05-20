import type AppSession from "../entities/AppSession";
import UserLoggedIn from "../events/UserLoggedIn";
import Policy from "../../utils/Policy";

class UpdateAppSessionOnLogIn extends Policy {

    public static readonly TYPE = "UpdateAppSessionOnLogIn";

    public constructor(private appSession: AppSession) {
        super();
    }

    public execute = async (event: UserLoggedIn): Promise<void> => {
        this.appSession.authenticate(event.userInfo);
    }
}

export default UpdateAppSessionOnLogIn;