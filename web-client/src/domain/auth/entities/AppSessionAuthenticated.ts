import Subject from "../../../presentation/utils/binding/Subject";
import value from "../../../presentation/utils/binding/value";
import PersonalUserInfo from "../values/PersonalUserInfo";
import KeepType from "../../utils/KeepType.ts";

class AppSessionAuthenticated extends KeepType {

    public readonly userInfo: Subject<PersonalUserInfo>;

    public constructor(
        userInfo: PersonalUserInfo,
        private setAnonymousState: () => void
    ) {
        super();
        this.userInfo = value(userInfo);
    }

    public logout = async (): Promise<void> => {
        this.setAnonymousState();
    }

    public updateUserInfo = (newUserInfo: PersonalUserInfo) => {
        this.userInfo.set(newUserInfo);
    }
}

export default AppSessionAuthenticated;