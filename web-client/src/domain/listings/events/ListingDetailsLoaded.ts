import AppEvent from "../../utils/AppEvent.ts";
import ListingId from "../values/ListingId.ts";

class ListingDetailsLoaded extends AppEvent {

    public constructor(
        public readonly listingId: ListingId,
        public readonly details: any
    ) {
        super();
    }
}

export default ListingDetailsLoaded;