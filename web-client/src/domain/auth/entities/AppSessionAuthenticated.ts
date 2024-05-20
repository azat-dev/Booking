import Subject from "../../../presentation/utils/binding/Subject";
import value from "../../../presentation/utils/binding/value";
import PersonalUserInfo from "../values/PersonalUserInfo";

class AppSessionAuthenticated {

    public static readonly TYPE: string = 'AppSessionAuthenticated';

    public get type() {
        return (this.constructor as any).TYPE;
    }

    public readonly userInfo: Subject<PersonalUserInfo>;

    public constructor(
        userInfo: PersonalUserInfo,
        private setAnonymousState: () => void
    ) {
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