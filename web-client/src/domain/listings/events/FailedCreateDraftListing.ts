import AppEvent from "../../utils/AppEvent";

class FailedCreateDraftListing extends AppEvent {
    public constructor(
        public readonly error: Error,
        public readonly operationId: string
    ) {
        super();
    }
}

export default FailedCreateDraftListing;