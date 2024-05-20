import type AppSession from "../entities/AppSession";
import UserLoggedIn from "../events/UserLoggedIn";
import Policy from "../../utils/Policy";

class WhenLoggedInThenUpdateAppSession extends Policy {

    public static readonly TYPE = "WhenLoggedInThenUpdateAppSession";

    public constructor(private appSession: AppSession) {
        super();
    }

    public execute = async (event: UserLoggedIn): Promise<void> => {
        this.appSession.setAuthenticated(event.userInfo);
    }
}

export default WhenLoggedInThenUpdateAppSession;