import Subject from "../../../presentation/utils/binding/Subject";
import AppSessionAuthenticated from "./AppSessionAuthenticated";
import AppSessionAnonymous from "./AppSessionAnonymous";
import PersonalUserInfo from "../values/PersonalUserInfo";
import AppSessionLoading from "./AppSessionLoading";

export type SessionState = AppSessionAuthenticated | AppSessionAnonymous | AppSessionLoading;

interface AppSession {

    readonly state: Subject<SessionState>;

    authenticate: (userInfo: PersonalUserInfo) => void;

    logout: () => void;
}

export default AppSession;