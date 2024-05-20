import AppVM, {DialogVM} from "./AppVM";
import AuthorizedAppVM from "./AuthorizedAppVM";
import AnonymousAppVM from "./AnonymousAppVM";
import Subject from "../../utils/binding/Subject";
import value from "../../utils/binding/value";
import DialogsStore from "../../stores/DialogsStore";
import PagesModule from "../../../PagesModule";
import Bus from "../../../domain/utils/Bus";
import AccommodationId from "../../../domain/accommodations/AccommodationId";
import Page from "../Page";
import NavigationDelegate from "./NavigationDelegate";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";
import AppSession, {SessionState} from "../../../domain/auth/entities/AppSession";

class AppVmImpl implements AppVM {

    public state: AuthorizedAppVM | AnonymousAppVM;

    public readonly currentPage: Subject<Page | null>;
    public readonly activeDialog: Subject<DialogVM | null>;
    public navigationDelegate: NavigationDelegate | null = null;

    public constructor(
        appSession: AppSession,
        dialogsStore: DialogsStore,
        private readonly pages: PagesModule,
        private readonly bus: Bus
    ) {

        this.activeDialog = dialogsStore.activeDialog as any;
        this.currentPage = value({type: "loading"});


        this.state = this.makeState(appSession.state.value);
        appSession.state.listen((newState) => {
            this.state = this.makeState(newState);
        });
    }

    runProfilePage = async (): Promise<void> => {

        return this.state.runProfilePage();
    }

    runMainPage = async (): Promise<void> => {
        return this.state.runMainPage();
    }

    runAccommodationDetailsPage(id: AccommodationId): Promise<void> {
        return this.state.runAccommodationDetailsPage(id);
    }

    public makeAuthenticatedState = (session: AppSessionAuthenticated) => {
        return new AuthorizedAppVM(
            session,
            this.currentPage,
            this.activeDialog,
            this.pages,
            this.bus
        );
    }

    public makeAnonymousState = () => {
        return new AnonymousAppVM(
            this.currentPage,
            this.activeDialog,
            this.pages,
            this.bus
        );
    }

    public makeState = (sessionState: SessionState) => {
        if (sessionState.type === AppSessionAuthenticated.TYPE) {
            return this.makeAuthenticatedState(sessionState as any);
        }
        return this.makeAnonymousState();
    }

}

export default AppVmImpl;