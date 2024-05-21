import type {SessionState} from "../entities/AppSession";
import AppEvent from "../../utils/AppEvent";

class ChangedAppSessionState extends AppEvent {
    public constructor(private readonly newState: SessionState) {
        super();
    }
}

export default ChangedAppSessionState;