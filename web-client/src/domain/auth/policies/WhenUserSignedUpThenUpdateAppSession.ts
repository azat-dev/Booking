import type AppSession from "../entities/AppSession";
import Policy from "../../utils/Policy";
import UserSignedUpByEmail from "../events/UserSignedUpByEmail";

class WhenUserSignedUpThenUpdateAppSession extends Policy {

    public static readonly TYPE = "WHEN_USER_SIGNED_UP_THEN_UPDATE_APP_SESSION";

    public constructor(private appSession: AppSession) {
        super();
    }

    public execute = async (event: UserSignedUpByEmail): Promise<void> => {
        this.appSession.setAuthenticated(event.userInfo);
    }
}

export default WhenUserSignedUpThenUpdateAppSession;