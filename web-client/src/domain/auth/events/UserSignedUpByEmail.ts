import AppEvent from "../../utils/AppEvent";
import PersonalUserInfo from "../values/PersonalUserInfo";

class UserSignedUpByEmail extends AppEvent {

    public static readonly TYPE = "UserSignedUpByEmail";

    constructor(public readonly userInfo: PersonalUserInfo) {
        super();
    }
}

export default UserSignedUpByEmail;