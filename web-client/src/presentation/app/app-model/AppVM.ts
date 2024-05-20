import AccommodationId from "../../../domain/accommodations/AccommodationId";
import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
import SignUpDialogVM from "../../dialogs/sign-up-dialog/SignUpDialogVM";
import Subject from "../../utils/binding/Subject";
import NavigationDelegate from "./NavigationDelegate";
import Page from "../Page";



export interface InputAppVM {
    runProfilePage(): Promise<void>;

    runMainPage(): Promise<void>;

    runAccommodationDetailsPage(id: AccommodationId): Promise<void>;
}

export type DialogVM = LoginDialogVM | SignUpDialogVM;

export default interface AppVM extends InputAppVM {
    readonly currentPage: Subject<Page | null>;

    readonly activeDialog: Subject<DialogVM | null>;

    navigationDelegate: NavigationDelegate | null;

}
