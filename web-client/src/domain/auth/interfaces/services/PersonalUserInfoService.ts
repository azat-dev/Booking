import UserId from "../../values/UserId";
import PersonalUserInfo from "../../values/PersonalUserInfo";

interface PersonalUserInfoService {
    getUserInfo(id: UserId): Promise<PersonalUserInfo>;
}

export default PersonalUserInfoService;
