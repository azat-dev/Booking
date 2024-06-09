import AppEvent from "../../../utils/AppEvent.ts";
import PersonalUserInfo from "../../values/PersonalUserInfo.ts";

class UserSignedUpByEmail extends AppEvent {
    constructor(public readonly userInfo: PersonalUserInfo) {
        super();
    }
}

export default UserSignedUpByEmail;