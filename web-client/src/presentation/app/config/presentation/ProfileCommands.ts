import {Emit} from "../../../../domain/utils/Bus.ts";
import OpenFileDialogForUploadingUserPhoto from "../../../commands/OpenFileDialogForUploadingUserPhoto.ts";
import IdentityCommands from "../domain/IdentityCommands.ts";
import AppSessionAuthenticated from "../../../../domain/auth/entities/AppSessionAuthenticated.ts";


class ProfileCommands {

    public constructor(
        private readonly identityCommands: IdentityCommands,
        private readonly appSession: AppSessionAuthenticated,
        private readonly emit: Emit
    ) {
    }

    public openFileDialogForUploadingUserPhoto = () => {
        return new OpenFileDialogForUploadingUserPhoto(
            this.identityCommands.uploadNewUserPhoto,
            this.appSession,
            this.emit
        ).execute();
    }
}

export default ProfileCommands;