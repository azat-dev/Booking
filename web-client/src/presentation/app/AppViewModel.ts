import LoginDialogViewModel from "../dialogs/login-dialog/LoginDialogViewModel";
import SignUpDialogViewModel from "../dialogs/sign-up-dialog/SignUpDialogViewModel";
import PageAccommodationDetailsViewModel from "../pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import PageMainViewModel from "../pages/page-main/PageMainViewModel";
import Subject from "../utils/binding/Subject";

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
    activeDialog: Subject<ActiveDialogViewModel | null>;

    openLoginDialog(): void;
    openSignUpDialog(): void;

    toggleFavorite(id: string): void;

    makeMainPage(): PageMainViewModel;
    makeAccommodationDetailsPage(): PageAccommodationDetailsViewModel;
}
