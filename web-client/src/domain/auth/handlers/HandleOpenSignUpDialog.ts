import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import CommonDialogsConfig from "../../../presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import OpenSignUpDialog from "../../../presentation/commands/OpenSignUpDialog.ts";
import OpenedSignUpDialog from "../../../presentation/events/OpenedSignUpDialog.ts";

class HandleOpenSignUpDialog extends Handler {

    public constructor(
        private readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: OpenSignUpDialog): Promise<void> => {

        this.activeDialog.set(
            this.dialogs.signUpDialog()
        );
        this.bus.publish(new OpenedSignUpDialog().withSender(command.senderId));
    }
}

export default HandleOpenSignUpDialog;