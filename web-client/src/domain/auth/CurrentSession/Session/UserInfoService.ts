import UserInfo from "../../values/User";
import UserId from "../../values/UserId";

interface UserInfoService {
    getUserInfo(id: UserId): Promise<UserInfo>;
}

export default UserInfoService;
