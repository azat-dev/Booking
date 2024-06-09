import AppEvent from "../../utils/AppEvent";

class FailedCreateDraftListing extends AppEvent {
    public constructor(
        public readonly error: Error
    ) {
        super();
    }
}

export default FailedCreateDraftListing;