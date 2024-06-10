import Command from "../../utils/Command.ts";
import ListingId from "../values/ListingId.ts";


class LoadListingDetails extends Command {

    public constructor(
        public readonly listingId: ListingId,
    ) {
        super();
    }
}

export default LoadListingDetails;