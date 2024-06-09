import AppEvent from "../../utils/AppEvent.ts";
import ListingId from "../values/ListingId.ts";

export interface ListingUpdatedPayload {
    title?: string;
    description?: string;
    photos?: string[];
    status?: string;
}

class ListingDetailsUpdated extends AppEvent {

    public constructor(
        public readonly listingId: ListingId,
        public readonly payload: ListingUpdatedPayload
    ) {
        super();
    }
}

export default ListingDetailsUpdated;