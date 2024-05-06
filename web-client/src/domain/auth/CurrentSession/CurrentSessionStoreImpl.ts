import Subject from "../../../presentation/utils/binding/Subject";
import value from "../../../presentation/utils/binding/value";
import LocalAuthDataRepository from "../interfaces/repositories/LocalAuthDataRepository";
import CurrentSessionStore from "./CurrentSessionStore";
import AuthService from "./Session/AuthService";
import { Session } from "./Session/Session";
import SessionAnonymousImpl from "./Session/SessionAnonymousImpl";
import UserInfoService from "./Session/UserInfoService";

class CurrentSessionStoreImpl implements CurrentSessionStore {
    public current: Subject<Session>;

    public constructor(
        authService: AuthService,
        userInfoService: UserInfoService,
        localAuthDataRepository: LocalAuthDataRepository
    ) {
        this.current = value(
            new SessionAnonymousImpl(
                authService,
                userInfoService,
                localAuthDataRepository,
                (next) => {
                    this.current.set(next);
                }
            )
        );
    }

    public tryToLoadLastSession = async (): Promise<void> => {
        if (this.current.value.type !== "anonymous") {
            return;
        }

        this.current.value.tryToLoadLastSession();
    };
}

export default CurrentSessionStoreImpl;
