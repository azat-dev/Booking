import LoginDialogViewModel from "../dialogs/login-dialog/LoginDialogViewModel";
import Subject from "../utils/binding/Subject";

export type ActiveDialogViewModel = {
    type: "login";
    vm: LoginDialogViewModel;
};

export default interface AppViewModel {
    activeDialog: Subject<ActiveDialogViewModel | null>;

    openLoginDialog(): void;
    toggleFavorite(id: string): void;
}
