import PersonalUserInfo from "../../values/PersonalUserInfo.ts";
import AppEvent from "../../../utils/AppEvent.ts";

class UserLoggedIn extends AppEvent {
    public constructor(
        public readonly userInfo: PersonalUserInfo
    ) {
        super();
    }
}

export default UserLoggedIn;