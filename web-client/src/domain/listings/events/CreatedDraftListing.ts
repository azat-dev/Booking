import AppEvent from "../../utils/AppEvent";
import ListingId from "../values/ListingId.ts";

class CreatedDraftListing extends AppEvent {
    public constructor(
        public readonly listingId: ListingId,
        public readonly operationId: string
    ) {
        super();
    }
}

export default CreatedDraftListing;