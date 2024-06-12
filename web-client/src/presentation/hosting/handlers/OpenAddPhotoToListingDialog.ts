import {fileDialog} from "file-select-dialog";

import {Emit} from "../../../domain/utils/Bus.ts";
import UserClosedAddPhotoToListingDialog from "../events/UserClosedAddPhotoToListingDialog.ts";
import ListingId from "../../../domain/listings/values/ListingId.ts";

class OpenAddPhotoToListingDialog {

    public constructor(
        private readonly addNewPhotoToListing: (listingId: ListingId, file: File) => Promise<void>,
        private readonly emit: Emit
    ) {
    }

    public execute = async (listingId: ListingId): Promise<void> => {

        const files = await fileDialog({accept: ['.png', '.jpg', '.webp', '.jpeg']});
        if (files.length === 0) {
            this.emit(new UserClosedAddPhotoToListingDialog());
            return;
        }

        const file = files[0];
        await this.addNewPhotoToListing(listingId, file);
    }
}

export default OpenAddPhotoToListingDialog;