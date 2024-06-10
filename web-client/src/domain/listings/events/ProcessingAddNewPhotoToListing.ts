import ListingId from "../values/ListingId.ts";
import AppEvent from "../../utils/AppEvent.ts";

class ProcessingAddNewPhotoToListing extends AppEvent {
    public constructor(listingId: ListingId) {
        super();
    }
}

export default ProcessingAddNewPhotoToListing;