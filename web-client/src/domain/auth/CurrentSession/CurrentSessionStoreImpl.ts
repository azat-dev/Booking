import Subject from "../../../presentation/utils/binding/Subject";
import value from "../../../presentation/utils/binding/value";
import TokensRepository from "../interfaces/repositories/TokensRepository";
import CurrentSessionStore from "./CurrentSessionStore";
import AuthService from "./Session/AuthService";
import { Session } from "./Session/Session";
import SessionAnonymousImpl from "./Session/SessionAnonymousImpl";

class CurrentSessionStoreImpl implements CurrentSessionStore {
    public current: Subject<Session>;

    constructor(
        localTokensRepository: TokensRepository,
        authService: AuthService
    ) {
        this.current = value(
            new SessionAnonymousImpl(
                authService,
                localTokensRepository,
                (next) => {
                    this.current.set(next);
                }
            )
        );
    }
}

export default CurrentSessionStoreImpl;
