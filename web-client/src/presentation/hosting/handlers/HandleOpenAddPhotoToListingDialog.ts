import {fileDialog} from "file-select-dialog";

import Bus from "../../../domain/utils/Bus.ts";
import Handler from "../../../domain/utils/Handler.ts";
import OpenAddPhotoToListingDialog from "../commands/OpenAddPhotoToListingDialog.ts";
import UserClosedAddPhotoToListingDialog from "../events/UserClosedAddPhotoToListingDialog.ts";
import AddNewPhotoToListing from "../../../domain/listings/commands/AddNewPhotoToListing.ts";

class HandleOpenAddPhotoToListingDialog extends Handler {

    public constructor(
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: OpenAddPhotoToListingDialog): Promise<void> => {

        const files = await fileDialog({accept: ['.png', '.jpg', '.webp', '.jpeg']});
        if (files.length === 0) {
            this.bus.publish(
                new UserClosedAddPhotoToListingDialog()
                    .withSender(command.senderId)
            );
            return;
        }

        const file = files[0];
        this.bus.publish(
            new AddNewPhotoToListing(
                command.listingId,
                file,
            ).withSender(command.senderId)
        );
    }
}

export default HandleOpenAddPhotoToListingDialog;