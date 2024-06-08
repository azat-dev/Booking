import {Address, ListingStatus} from "../../../data/api/listings";

class Listing {

    constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly address: Address,
        public readonly status: ListingStatus
    ) {
    }
}

export default Listing;