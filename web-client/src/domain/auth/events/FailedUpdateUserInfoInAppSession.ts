import AppEvent from "../../utils/AppEvent";

class FailedUpdateUserInfoInAppSession extends AppEvent {
    public constructor(error: Error) {
        super();
    }
}

export default FailedUpdateUserInfoInAppSession;