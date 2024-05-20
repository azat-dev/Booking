import AccommodationId from "../../../domain/accommodations/AccommodationId";
import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
import SignUpDialogVM from "../../dialogs/sign-up-dialog/SignUpDialogVM";
import Subject from "../../utils/binding/Subject";
import NavigationDelegate from "./NavigationDelegate";
import Page from "../Page";

export enum ActiveDialogType {
    Login = "login",
    SignUp = "sign-up",
}

export type ActiveDialogVM =
    | {
    type: ActiveDialogType.Login;
    vm: LoginDialogVM;
}
    | {
    type: ActiveDialogType.SignUp;
    vm: SignUpDialogVM;
};


export interface InputAppVM {
    runProfilePage(): Promise<void>;

    runMainPage(): Promise<void>;

    runAccommodationDetailsPage(id: AccommodationId): Promise<void>;
}

export default interface AppVM extends InputAppVM {
    readonly currentPage: Subject<Page | null>;

    readonly activeDialog: Subject<ActiveDialogVM | null>;

    navigationDelegate: NavigationDelegate | null;

}
