import AppEvent from "../../utils/AppEvent.ts";
import ListingId from "../values/ListingId.ts";


class FailedToLoadListingDetails extends AppEvent {

    public constructor(listingId: ListingId, error: any) {
        super();
    }
}

export default FailedToLoadListingDetails;