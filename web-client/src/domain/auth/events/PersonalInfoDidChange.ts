import PersonalUserInfo from "../values/PersonalUserInfo";
import AppEvent from "../../utils/AppEvent";

class PersonalInfoDidChange extends AppEvent {

    public static readonly TYPE = "PersonalInfoDidChange";

    public constructor(public readonly newPersonalInfo: PersonalUserInfo) {
        super();
    }
}

export default PersonalInfoDidChange;