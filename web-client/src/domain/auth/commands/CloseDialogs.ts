import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import LoginDialogVM from "../../../presentation/dialogs/login-dialog/LoginDialogVM.ts";
import LoginDialogDidClose from "../events/LoginDialogDidClose.ts";
import SignUpDialogVM from "../../../presentation/dialogs/sign-up-dialog/SignUpDialogVM.ts";
import SignUpDialogDidClose from "../events/SignUpDialogDidClose.ts";
import {Emit} from "../../utils/Bus.ts";

class CloseDialogs {

    public constructor(
        private readonly activeDialog: Subject<DialogVM | null>,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<void> => {

        const exisitingDialogType = this.activeDialog.value?.type;
        if (!exisitingDialogType) {
            return;
        }

        this.activeDialog.set(null);

        switch (exisitingDialogType) {
            case LoginDialogVM.type:
                this.emit(new LoginDialogDidClose());
                return;
            case SignUpDialogVM.type:
                this.emit(new SignUpDialogDidClose());
                return;
        }
    }
}

export default CloseDialogs;