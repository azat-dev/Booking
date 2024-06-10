import Command from "../../utils/Command.ts";
import ListingId from "../values/ListingId.ts";

class AddNewPhotoToListing extends Command {
    public constructor(
        public readonly listingId: ListingId,
        public readonly file: File,
    ) {
        super();
    }
}

export default AddNewPhotoToListing;