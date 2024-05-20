import Bus from "../../domain/utils/Bus";
import DialogsModule from "../app/app-model/DialogsModule";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import OpenLoginDialog from "../commands/OpenLoginDialog";
import CloseDialog from "../commands/CloseDialog";
import Command from "../../domain/utils/Command";
import OpenSignUpDialog from "../commands/OpenSignUpDialog";
import OpenedLoginDialog from "../events/OpenedLoginDialog";
import OpenedSignUpDialog from "../events/OpenedSignUpDialog";

class DialogsStore {

    public readonly activeDialog: Subject<any | null>;

    public constructor(
        private readonly dialogs: DialogsModule,
        private readonly bus: Bus
    ) {
        this.activeDialog = value(null);
    }

    public closeDialogs = () => {
        this.activeDialog.set(null);
    }

    public handle = (event: Command) => {

        switch (event.type) {
            case OpenLoginDialog.TYPE: {
                const vm = this.dialogs.loginDialog();
                this.activeDialog.set(vm);
                this.bus.publish(new OpenedLoginDialog());
                return;
            }

            case OpenSignUpDialog.TYPE: {
                const vm = this.dialogs.signUpDialog();
                this.activeDialog.set(vm);
                this.bus.publish(new OpenedSignUpDialog());
                return;
            }

            case CloseDialog.TYPE:
                this.closeDialogs();
                return;
        }
    }
}

export default DialogsStore;