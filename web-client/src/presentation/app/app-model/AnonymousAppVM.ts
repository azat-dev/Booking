import AppVM, {DialogVM} from "./AppVM";
import NavigationDelegate from "./NavigationDelegate";
import AccommodationId from "../../../domain/accommodations/AccommodationId";
import OpenLoginDialog from "../../commands/OpenLoginDialog";
import Bus from "../../../domain/utils/Bus";
import PagesModule from "../../../PagesModule";
import Subject from "../../utils/binding/Subject";
import Page from "../Page";
import value from "../../utils/binding/value";
import DialogsStore from "../../stores/DialogsStore";

class AnonymousAppVM implements AppVM {

    public currentPage: Subject<Page | null>;
    public readonly activeDialog: Subject<DialogVM | null>;
    public navigationDelegate: NavigationDelegate | null = null;

    public constructor(
        dialogsStore: DialogsStore,
        private readonly pages: PagesModule,
        private readonly bus: Bus
    ) {

        this.activeDialog = dialogsStore.activeDialog as any;
        this.currentPage = value({type: "loading"});
    }

    runProfilePage = async (): Promise<void> => {

        this.navigationDelegate?.navigateToMainPage(true);
        await this.openLoginDialog();
    }

    runMainPage = async (): Promise<void> => {
        this.currentPage.set({
            type: "main",
            vm: this.pages.mainPage()
        });
    }

    runAccommodationDetailsPage(id: AccommodationId): Promise<void> {
        throw new Error("Method not implemented.");
    }

    public openLoginDialog = async (): Promise<void> => {
        this.bus.publish(new OpenLoginDialog());
    };
}

export default AnonymousAppVM;