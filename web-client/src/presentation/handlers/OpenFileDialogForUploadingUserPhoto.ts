import {fileDialog} from "file-select-dialog";
import {Emit} from "../../domain/utils/Bus";
import UserClosedFileDialogForUploadingUserPhoto from "../events/UserClosedFileDialogForUploadingUserPhoto";
import AppSessionAuthenticated from "../../domain/auth/entities/AppSessionAuthenticated.ts";
import UserId from "../../domain/auth/values/UserId.ts";

class OpenFileDialogForUploadingUserPhoto {

    public constructor(
        private readonly uploadNewUserPhoto: (userId: UserId, file: File) => Promise<void>,
        private readonly appSession: AppSessionAuthenticated,
        private readonly emit: Emit,
    ) {
    }

    public execute = async () => {

        const userId = this.appSession.userInfo.value.id;

        const files = await fileDialog({accept: ['.png', '.jpg', '.webp', '.jpeg']});
        if (files.length === 0) {
            this.emit(new UserClosedFileDialogForUploadingUserPhoto());
            return;
        }

        const file = files[0];
        await this.uploadNewUserPhoto(userId, file);
    }
}

export default OpenFileDialogForUploadingUserPhoto;