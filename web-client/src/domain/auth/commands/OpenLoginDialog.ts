import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import CommonDialogsConfig from "../../../presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import OpenedLoginDialog from "../../../presentation/events/OpenedLoginDialog.ts";
import {Emit} from "../../utils/Bus.ts";

class OpenLoginDialog {

    public constructor(
        public readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<void> => {

        this.activeDialog.set(this.dialogs.loginDialog());
        this.emit(new OpenedLoginDialog());
    }
}

export default OpenLoginDialog;