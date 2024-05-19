import AccommodationId from "../../../domain/accommodations/AccommodationId";
import LoginDialogViewModel from "../../dialogs/login-dialog/LoginDialogViewModel";
import SignUpDialogViewModel from "../../dialogs/sign-up-dialog/SignUpDialogViewModel";
import Subject from "../../utils/binding/Subject";
import NavigationDelegate from "./NavigationDelegate";
import Page from "../Page";

export enum ActiveDialogType {
    Login = "login",
    SignUp = "sign-up",
}

export type ActiveDialogViewModel =
    | {
    type: ActiveDialogType.Login;
    vm: LoginDialogViewModel;
}
    | {
    type: ActiveDialogType.SignUp;
    vm: SignUpDialogViewModel;
};


export default interface AppViewModel {
    readonly currentPage: Subject<Page | null>;

    readonly activeDialog: Subject<ActiveDialogViewModel | null>;

    navigationDelegate: NavigationDelegate | null;

    runProfilePage(): Promise<void>;

    runMainPage(): Promise<void>;

    runAccommodationDetailsPage(id: AccommodationId): Promise<void>;
}
