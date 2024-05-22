import RouterVM from "./router/RouterVM.tsx";
import Subject from "../utils/binding/Subject.ts";
import value from "../utils/binding/value.ts";
import LoginDialogVM from "../dialogs/login-dialog/LoginDialogVM.ts";
import SignUpDialogVM from "../dialogs/sign-up-dialog/SignUpDialogVM.ts";

export type DialogVM = LoginDialogVM | SignUpDialogVM;

class AppVM {

    public readonly activeDialog: Subject<DialogVM | null>;

    public constructor(
        public readonly router: RouterVM
    ) {

        this.activeDialog = value(null);
    }
}

export default AppVM;