import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import Subject from "../../../presentation/utils/binding/Subject.ts";
import {DialogVM} from "../../../presentation/app/AppVM.tsx";
import CommonDialogsConfig from "../../../presentation/app/config/presentation/common/CommonDialogsConfig.ts";
import OpenedLoginDialog from "../../../presentation/events/OpenedLoginDialog.ts";
import OpenLoginDialog from "../../../presentation/commands/OpenLoginDialog.ts";

class HandleOpenLoginDialog extends Handler {

    public constructor(
        public readonly activeDialog: Subject<DialogVM | null>,
        private readonly dialogs: CommonDialogsConfig,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: OpenLoginDialog): Promise<void> => {

        this.activeDialog.set(this.dialogs.loginDialog());
        this.bus.publish(new OpenedLoginDialog().withSender(command.senderId));
    }
}

export default HandleOpenLoginDialog;