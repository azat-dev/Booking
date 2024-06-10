import OpenFileDialogForUploadingUserPhoto from "../commands/OpenFileDialogForUploadingUserPhoto";
import {fileDialog} from "file-select-dialog";
import Bus from "../../domain/utils/Bus";
import UploadNewUserPhoto from "../../domain/auth/commands/UploadNewUserPhoto";
import UserClosedFileDialogForUploadingUserPhoto from "../events/UserClosedFileDialogForUploadingUserPhoto";
import AppSession from "../../domain/auth/entities/AppSession.ts";
import AppSessionAuthenticated from "../../domain/auth/entities/AppSessionAuthenticated.ts";
import Handler from "../../domain/utils/Handler.ts";

class OpenFileDialogForUploadingUserPhotoHandler extends Handler {

    public constructor(
        private readonly appSession: AppSession,
        private readonly bus: Bus,
    ) {
        super();
    }

    public execute = async (command: OpenFileDialogForUploadingUserPhoto) => {

        const session = this.appSession.state.value;
        const isUserLoggedIn = session instanceof AppSessionAuthenticated;

        if (!isUserLoggedIn) {
            this.bus.publish(
                new UserClosedFileDialogForUploadingUserPhoto()
                    .withSender(command.senderId)
            );
            return;
        }

        const userId = session.userInfo.value.id;

        const files = await fileDialog({accept: ['.png', '.jpg', '.webp', '.jpeg']});
        if (files.length === 0) {
            this.bus.publish(
                new UserClosedFileDialogForUploadingUserPhoto()
                    .withSender(command.senderId)
            );
            return;
        }

        const file = files[0];
        this.bus.publish(
            new UploadNewUserPhoto(
                userId,
                file,
            ).withSender(command.senderId)
        );
    }
}

export default OpenFileDialogForUploadingUserPhotoHandler;