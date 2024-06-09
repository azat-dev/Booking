import AppEvent from "../../utils/AppEvent.ts";
import ListingId from "../values/ListingId.ts";

class ListingDraftCreated extends AppEvent {
    public constructor(
        public readonly listingId: ListingId,
        public readonly operationId: string
    ) {
        super();
    }
}

export default ListingDraftCreated;