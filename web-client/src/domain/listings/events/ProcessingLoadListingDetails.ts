import AppEvent from "../../utils/AppEvent.ts";
import ListingId from "../values/ListingId.ts";

class ProcessingLoadListingDetails extends AppEvent {

    public constructor(listingId: ListingId) {
        super();
    }
}

export default ProcessingLoadListingDetails;