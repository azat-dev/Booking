import AppEvent from "../../../domain/utils/AppEvent.ts";

class OpenedAddPhotoToListingDialog extends AppEvent {

    public constructor(
    ) {
        super();
    }
}

export default OpenedAddPhotoToListingDialog;