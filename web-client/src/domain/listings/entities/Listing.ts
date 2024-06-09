import {ListingStatus} from "../../../data/api/listings";
import ListingId from "../values/ListingId.ts";
import ListingDescription from "../values/ListingDescription.ts";
import ListingTitle from "../values/ListingTitle.ts";

class Listing {

    constructor(
        public readonly id: ListingId,
        public readonly title: ListingTitle,
        public readonly description: ListingDescription | null
    ) {
    }

    public getStatus = (): ListingStatus => {
        return ListingStatus.Draft;
    }
}

export default Listing;