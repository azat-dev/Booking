import UserId from "../../values/UserId";
import PersonalUserInfo from "../../values/PersonalUserInfo";

interface PersonalUserInfoService {
    getUserInfo(id: UserId): Promise<PersonalUserInfo>;

    updateUserPhoto(id: UserId, photo: File): Promise<void>;
}

export default PersonalUserInfoService;
