import ListingId from "../values/ListingId.ts";
import AppEvent from "../../utils/AppEvent.ts";

class FailedToAddNewPhotoToListing extends AppEvent {

    public constructor(
        public readonly listingId: ListingId,
        public readonly error: any
    ) {
        super();
    }
}

export default FailedToAddNewPhotoToListing;