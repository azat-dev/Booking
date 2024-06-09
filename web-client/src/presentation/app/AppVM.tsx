import RouterVM from "./router/RouterVM.tsx";
import Subject from "../utils/binding/Subject.ts";
import LoginDialogVM from "../dialogs/login-dialog/LoginDialogVM.ts";
import SignUpDialogVM from "../dialogs/sign-up-dialog/SignUpDialogVM.ts";
import VM from "../utils/VM.ts";

export type DialogVM = LoginDialogVM | SignUpDialogVM;

class AppVM extends VM {

    public constructor(
        public readonly router: RouterVM,
        public readonly activeDialog: Subject<DialogVM | null>
    ) {

        super();
    }
}

export default AppVM;