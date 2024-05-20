import PersonalUserInfo from "../values/PersonalUserInfo";
import AppEvent from "../../utils/AppEvent";

class PersonalInfoDidChange extends AppEvent {

    public static readonly TYPE = "PERSONAL_INFO_DID_CHANGE";

    public constructor(public readonly newPersonalInfo: PersonalUserInfo) {
        super();
    }
}

export default PersonalInfoDidChange;