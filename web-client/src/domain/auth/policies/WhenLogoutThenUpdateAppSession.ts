import UserLoggedOut from "../events/UserLoggedOut";
import AppSessionAuthenticated from "../entities/AppSessionAuthenticated";
import type AppSession from "../entities/AppSession";
import Policy from "../../utils/Policy";

class WhenLogoutThenUpdateAppSession extends Policy {
    public constructor(private appSession: AppSession) {
        super();
    }

    public execute = async (event: UserLoggedOut): Promise<void> => {
        const currentState = this.appSession.state.value;
        const isAuthenticated = currentState instanceof AppSessionAuthenticated;

        if (!isAuthenticated) {
            return;
        }

        await currentState.logout();
    }
}

export default WhenLogoutThenUpdateAppSession;