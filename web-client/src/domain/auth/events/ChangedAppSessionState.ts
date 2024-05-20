import type {SessionState} from "../entities/AppSession";
import AppEvent from "../../utils/AppEvent";

class ChangedAppSessionState extends AppEvent {

    public static readonly TYPE = "CHANGED_APP_SESSION_STATE";
    public constructor(private readonly newState: SessionState) {
        super();
    }
}

export default ChangedAppSessionState;