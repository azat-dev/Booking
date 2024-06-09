import AppEvent from "../../../../utils/AppEvent.ts";

class FailedLoginByToken extends AppEvent {

    public constructor(
        public readonly error: any
    ) {
        super();
    }
}

export default FailedLoginByToken;