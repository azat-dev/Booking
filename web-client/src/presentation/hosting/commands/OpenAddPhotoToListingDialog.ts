import ListingId from "../../../domain/listings/values/ListingId.ts";
import Command from "../../../domain/utils/Command.ts";


class OpenAddPhotoToListingDialog extends Command {

    public constructor(
        public readonly listingId: ListingId,
    ) {
        super();
    }
}

export default OpenAddPhotoToListingDialog;