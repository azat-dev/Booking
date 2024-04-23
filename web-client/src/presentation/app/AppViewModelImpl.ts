import AppViewModel, { ActiveDialogViewModel } from "./AppViewModel";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import CurrentSessionStore from "../../domain/auth/CurrentSession/CurrentSessionStore";
import CurrentSessionStoreImpl from "../../domain/auth/CurrentSession/CurrentSessionStoreImpl";
import LocalStorageTokensRepository from "../../LocalStorageTokensRepository";
import AuthServiceImpl from "../../data/auth/services/AuthServiceImpl";
import AuthDialogViewModel from "../dialogs/auth-dialog/AuthDialogViewModel";
import SessionStatus from "../../domain/auth/CurrentSession/Session/SessionStatus";

class AppViewModelImpl implements AppViewModel {
    public activeDialog: Subject<ActiveDialogViewModel | null>;
    private currentSession: CurrentSessionStore;

    public constructor() {
        this.activeDialog = value(null);

        const tokensRepository = new LocalStorageTokensRepository();
        const authService = new AuthServiceImpl();

        this.currentSession = new CurrentSessionStoreImpl(
            tokensRepository,
            authService
        );
    }

    private closeDialog = (): void => {
        this.activeDialog.set(null);
    };

    public openLoginDialog = (): void => {
        this.activeDialog.set({
            type: "login",
            vm: new AuthDialogViewModel(async (email, password) => {
                const session = this.currentSession.current.value;

                if (session.type !== SessionStatus.ANONYMOUS) {
                    return;
                }

                await session.authenticate(email, password);
            }, this.closeDialog),
        });
    };
}

export default AppViewModelImpl;
