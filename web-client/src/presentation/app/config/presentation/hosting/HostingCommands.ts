import Bus from "../../../../../domain/utils/Bus.ts";
import ListingsCommands from "../../domain/ListingsCommands.ts";
import OpenAddPhotoToListingDialog from "../../../../hosting/handlers/OpenAddPhotoToListingDialog.ts";
import ListingId from "../../../../../domain/listings/values/ListingId.ts";

class HostingCommands {

    public constructor(
        private readonly listingsCommands: ListingsCommands,
        private readonly bus: Bus,
    ) {
    }

    public openAddPhotoToListingDialog = (listingId: ListingId) => {
        return new OpenAddPhotoToListingDialog(
            this.listingsCommands.addNewPhotoToListing,
            this.bus.publish
        ).execute(listingId);
    }
}

export default HostingCommands;