import type AppSession from "../entities/AppSession";
import UserLoggedIn from "../events/login/UserLoggedIn.ts";
import Policy from "../../utils/Policy";

class WhenLoggedInThenUpdateAppSession extends Policy {

    public constructor(private appSession: AppSession) {
        super();
    }

    public execute = async (event: UserLoggedIn): Promise<void> => {
        this.appSession.setAuthenticated(event.userInfo);
    }
}

export default WhenLoggedInThenUpdateAppSession;