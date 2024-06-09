import AppEvent from "../../utils/AppEvent";

class FailedToLoadOwnListings extends AppEvent {

    public constructor(
        public readonly error: Error,
        public readonly operationId: string
    ) {
        super();
    }
}

export default FailedToLoadOwnListings;