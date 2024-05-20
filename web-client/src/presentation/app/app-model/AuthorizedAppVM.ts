import {DialogVM, InputAppVM} from "./AppVM";
import NavigationDelegate from "./NavigationDelegate";
import AccommodationId from "../../../domain/accommodations/AccommodationId";
import Bus from "../../../domain/utils/Bus";
import PagesModule from "../../../PagesModule";
import Subject from "../../utils/binding/Subject";
import Page from "../Page";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";

class AuthorizedAppVM implements InputAppVM {

    public static readonly TYPE = "AUTHORIZED_APP_VM";

    public navigationDelegate: NavigationDelegate | null = null;

    public constructor(
        private readonly session: AppSessionAuthenticated,
        private readonly currentPage: Subject<Page | null>,
        private readonly activeDialog: Subject<DialogVM | null>,
        private readonly pages: PagesModule,
        private readonly bus: Bus
    ) {

        activeDialog.set(null);
    }

    public get type() {
        return AuthorizedAppVM.TYPE;
    }

    runProfilePage = async (): Promise<void> => {

        this.currentPage.set(this.pages.profilePage(this.session));
    }

    runMainPage = async (): Promise<void> => {
        this.currentPage.set(this.pages.mainPage());
    }

    runAccommodationDetailsPage(id: AccommodationId): Promise<void> {
        throw new Error("Method not implemented.");
    }
}

export default AuthorizedAppVM;