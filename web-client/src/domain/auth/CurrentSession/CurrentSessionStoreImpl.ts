import Subject from "../../../presentation/utils/binding/Subject";
import value from "../../../presentation/utils/binding/value";
import TokensRepository from "../interfaces/repositories/TokensRepository";
import CurrentSessionStore from "./CurrentSessionStore";
import AuthService from "./Session/AuthService";
import { Session } from "./Session/Session";
import SessionAnonymousImpl from "./Session/SessionAnonymousImpl";
import UserInfoService from "./Session/UserInfoService";

class CurrentSessionStoreImpl implements CurrentSessionStore {
    public current: Subject<Session>;

    constructor(
        authService: AuthService,
        userInfoService: UserInfoService,
        localTokensRepository: TokensRepository
    ) {
        this.current = value(
            new SessionAnonymousImpl(
                authService,
                userInfoService,
                localTokensRepository,
                (next) => {
                    this.current.set(next);
                }
            )
        );
    }
}

export default CurrentSessionStoreImpl;
