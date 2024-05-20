import AppEvent from "../../utils/AppEvent";
import PersonalUserInfo from "../values/PersonalUserInfo";

class UserSignedUpByEmail extends AppEvent {

    public static readonly TYPE = "USER_SIGNED_UP_BY_EMAIL";

    constructor(public readonly userInfo: PersonalUserInfo) {
        super();
    }
}

export default UserSignedUpByEmail;