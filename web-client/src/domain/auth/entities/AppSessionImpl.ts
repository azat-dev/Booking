import AppSession from "./AppSession";
import AppSessionAuthenticated from "./AppSessionAuthenticated";
import Subject from "../../../presentation/utils/binding/Subject";
import AppSessionAnonymous from "./AppSessionAnonymous";
import AppSessionLoading from "./AppSessionLoading";
import value from "../../../presentation/utils/binding/value";
import PersonalUserInfo from "../values/PersonalUserInfo";
import Bus from "../../utils/Bus";
import ChangedAppSessionState from "../events/ChangedAppSessionState";

class AppSessionImpl implements AppSession {

    public constructor(private readonly bus: Bus) {
    }

    public readonly state: Subject<AppSessionAuthenticated | AppSessionAnonymous | AppSessionLoading> = value(new AppSessionLoading());

    public setAuthenticated = (userInfo: PersonalUserInfo): void => {
        const newState = new AppSessionAuthenticated(userInfo, this.setAnonymous);
        this.state.set(newState);
        this.bus.publish(new ChangedAppSessionState(newState));
    }

    public setAnonymous = (): void => {
        const newState = new AppSessionAnonymous();
        this.state.set(newState);
        this.bus.publish(new ChangedAppSessionState(newState));
    }
}

export default AppSessionImpl;