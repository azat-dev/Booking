import AppEvent from "../../utils/AppEvent";
import ListingId from "../values/ListingId.ts";

class FailedUpdateListingDetails extends AppEvent {

    public constructor(
        public readonly listingId: ListingId,
        public readonly error: Error,
        public readonly operationId: string
    ) {
        super();
    }
}

export default FailedUpdateListingDetails;