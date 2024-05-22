import Handler from "../../utils/Handler.ts";
import UploadNewUserPhoto from "../commands/UploadNewUserPhoto.ts";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService.ts";
import Bus from "../../utils/Bus.ts";
import UpdatedUserPhoto from "../events/UpdatedUserPhoto.ts";

class UploadNewUserPhotoHandler extends Handler {

    public constructor(
        private readonly infoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: UploadNewUserPhoto): Promise<void> => {
        await this.infoService.updateUserPhoto(command.userId, command.file);
        this.bus.publish(new UpdatedUserPhoto());
    }
}

export default UploadNewUserPhotoHandler;