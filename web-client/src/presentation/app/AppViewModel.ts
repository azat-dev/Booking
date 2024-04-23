import AuthDialogViewModel from "../dialogs/auth-dialog/AuthDialogViewModel";
import Subject from "../utils/binding/Subject";

export type ActiveDialogViewModel = {
    type: "login";
    vm: AuthDialogViewModel;
};

export default interface AppViewModel {
    activeDialog: Subject<ActiveDialogViewModel | null>;

    openLoginDialog(): void;
    toggleFavorite(id: string): void;
}
