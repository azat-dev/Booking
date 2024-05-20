import PersonalUserInfo from "../values/PersonalUserInfo";
import AppEvent from "../../utils/AppEvent";

class UserLoggedIn extends AppEvent {

    public static readonly TYPE = "UserLoggedIn";

    public constructor(
        public readonly userInfo: PersonalUserInfo
    ) {
        super();
    }
}

export default UserLoggedIn;