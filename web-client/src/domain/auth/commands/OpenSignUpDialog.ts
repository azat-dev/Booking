import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import CommonDialogsConfig from "../../../presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import OpenedSignUpDialog from "../../../presentation/events/OpenedSignUpDialog.ts";
import {Emit} from "../../utils/Bus.ts";

class OpenSignUpDialog {

    public constructor(
        private readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<void> => {

        this.activeDialog.set(
            this.dialogs.signUpDialog()
        );
        this.emit(new OpenedSignUpDialog());
    }
}

export default OpenSignUpDialog;