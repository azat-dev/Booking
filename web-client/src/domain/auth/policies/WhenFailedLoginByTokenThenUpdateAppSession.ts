import AppSession from "../entities/AppSession";
import Policy from "../../utils/Policy";

class WhenFailedLoginByTokenThenUpdateAppSession extends Policy {

    public static readonly TYPE = "WhenFailedLoginByTokenThenUpdateAppSession";

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