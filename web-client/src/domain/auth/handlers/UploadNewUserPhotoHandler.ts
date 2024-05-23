import Handler from "../../utils/Handler.ts";
import UploadNewUserPhoto from "../commands/UploadNewUserPhoto.ts";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService.ts";
import Bus from "../../utils/Bus.ts";
import UpdatedUserPhoto from "../events/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../events/FailedUpdateUserPhoto.ts";

class UploadNewUserPhotoHandler extends Handler {

    public constructor(
        private readonly infoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: UploadNewUserPhoto): Promise<void> => {
        try {
            await this.infoService.updateUserPhoto(command.userId, command.file);
            this.bus.publish(new UpdatedUserPhoto());
        } catch (e) {
            console.info(e);
            this.bus.publish(new FailedUpdateUserPhoto());
        }
    }
}

export default UploadNewUserPhotoHandler;