import AppEvent from "../../utils/AppEvent";

class FailedToLoadOwnListings extends AppEvent {

    public constructor(
        public readonly error: Error
    ) {
        super();
    }
}

export default FailedToLoadOwnListings;