import AppEvent from "../../utils/AppEvent";

class FailedSignUpByEmail extends AppEvent {

    constructor(public readonly error: any) {
        super();
    }
}

export default FailedSignUpByEmail;