import Command from "../../utils/Command";
import ListingId from "../values/ListingId.ts";

export interface UpdateListingDetailsPayload {
    title?: string;
    description?: string;
    status?: string;
}

class UpdateListingDetails extends Command {

    public constructor(
        public readonly listingId: ListingId,
        public readonly payload: UpdateListingDetailsPayload

    ) {
        super();
    }
}

export default UpdateListingDetails;