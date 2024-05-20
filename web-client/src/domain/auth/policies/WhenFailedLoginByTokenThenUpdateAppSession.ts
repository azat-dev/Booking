import AppSession from "../entities/AppSession";
import Policy from "../../utils/Policy";

class WhenFailedLoginByTokenThenUpdateAppSession extends Policy {

    public static readonly TYPE = "WHEN_FAILED_LOGIN_BY_TOKEN_THEN_UPDATE_APP_SESSION";

    public constructor(
        private readonly appSession: AppSession
    ) {
        super();
    }

    public execute = (): void => {
        this.appSession.setAnonymous();
    }
}

export default WhenFailedLoginByTokenThenUpdateAppSession;