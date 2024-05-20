import AppSession from "./AppSession";
import AppSessionAuthenticated from "./AppSessionAuthenticated";
import Subject from "../../../presentation/utils/binding/Subject";
import AppSessionAnonymous from "./AppSessionAnonymous";
import AppSessionLoading from "./AppSessionLoading";
import value from "../../../presentation/utils/binding/value";
import PersonalUserInfo from "../values/PersonalUserInfo";

class AppSessionImpl implements AppSession {

    public readonly state: Subject<AppSessionAuthenticated | AppSessionAnonymous | AppSessionLoading> = value(new AppSessionLoading());

    public authenticate = (userInfo: PersonalUserInfo): void => {
        this.state.set(new AppSessionAuthenticated(userInfo, this.logout));
    }

    public logout = (): void => {
        this.state.set(new AppSessionAnonymous());
    }
}

export default AppSessionImpl;