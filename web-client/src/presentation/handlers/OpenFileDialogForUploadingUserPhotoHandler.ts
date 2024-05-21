import OpenFileDialogForUploadingUserPhoto from "../commands/OpenFileDialogForUploadingUserPhoto";
import {fileDialog} from "file-select-dialog";
import Bus from "../../domain/utils/Bus";
import UploadNewUserPhoto from "../../domain/auth/commands/UploadNewUserPhoto";
import UserClosedFileDialogForUploadingUserPhoto from "../events/UserClosedFileDialogForUploadingUserPhoto";

class OpenFileDialogForUploadingUserPhotoHandler {

    public constructor(private readonly bus: Bus) {
    }

    public handle = async (command: OpenFileDialogForUploadingUserPhoto) => {

        const files = await fileDialog({accept: ['.png', '.jpg', '.webp', '.jpeg']});
        if (files.length === 0) {
            this.bus.publish(new UserClosedFileDialogForUploadingUserPhoto());
            return;
        }

        let file = files[0];
        this.bus.publish(new UploadNewUserPhoto(file));

    }
}

export default OpenFileDialogForUploadingUserPhotoHandler;