import RouterVM from "./router/RouterVM.tsx";
import Subject from "../utils/binding/Subject.ts";
import LoginDialogVM from "../dialogs/login-dialog/LoginDialogVM.ts";
import SignUpDialogVM from "../dialogs/sign-up-dialog/SignUpDialogVM.ts";

export type DialogVM = LoginDialogVM | SignUpDialogVM;

class AppVM {

    public constructor(
        public readonly router: RouterVM,
        public readonly activeDialog: Subject<DialogVM | null>
    ) {
    }
}

export default AppVM;