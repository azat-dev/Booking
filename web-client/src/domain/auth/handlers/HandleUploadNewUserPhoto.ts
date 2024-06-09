import Handler from "../../utils/Handler.ts";
import UploadNewUserPhoto from "../commands/UploadNewUserPhoto.ts";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService.ts";
import Bus from "../../utils/Bus.ts";
import UpdatedUserPhoto from "../events/personal-info/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../events/personal-info/FailedUpdateUserPhoto.ts";
import UploadingUserPhoto from "../events/personal-info/UploadingUserPhoto.ts";

class HandleUploadNewUserPhoto extends Handler {

    public constructor(
        private readonly infoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: UploadNewUserPhoto): Promise<void> => {
        try {
            this.bus.publish(new UploadingUserPhoto().withSender(command.senderId));
            await this.infoService.updateUserPhoto(command.userId, command.file);
            this.bus.publish(new UpdatedUserPhoto().withSender(command.senderId));
        } catch (e) {
            console.info(e);
            this.bus.publish(new FailedUpdateUserPhoto().withSender(command.senderId));
        }
    }
}

export default HandleUploadNewUserPhoto;