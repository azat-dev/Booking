import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService.ts";
import {Emit} from "../../utils/Bus.ts";
import UpdatedUserPhoto from "../events/personal-info/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../events/personal-info/FailedUpdateUserPhoto.ts";
import UploadingUserPhoto from "../events/personal-info/UploadingUserPhoto.ts";
import UserId from "../values/UserId.ts";

class UploadNewUserPhoto {

    public constructor(
        private readonly infoService: PersonalUserInfoService,
        private readonly emit: Emit
    ) {
    }

    public execute = async (userId: UserId, file: File): Promise<void> => {
        try {
            this.emit(new UploadingUserPhoto());
            await this.infoService.updateUserPhoto(userId, file);
            this.emit(new UpdatedUserPhoto());
        } catch (e) {
            console.info(e);
            const errMsg = new FailedUpdateUserPhoto();
            this.emit(errMsg);
            throw errMsg;
        }
    }
}

export default UploadNewUserPhoto;