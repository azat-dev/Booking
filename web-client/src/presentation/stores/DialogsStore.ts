import Bus from "../../domain/utils/Bus";
import DialogsModule from "../app/app-model/DialogsModule";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import OpenLoginDialog from "../commands/OpenLoginDialog";
import CloseDialog from "../commands/CloseDialog";
import Command from "../../domain/utils/Command";

class DialogsStore {

    public readonly activeDialog: Subject<{ type: string, vm: any } | null>;

    public constructor(
        private readonly dialogs: DialogsModule,
        private readonly bus: Bus
    ) {
        this.activeDialog = value(null);
    }

    public closeDialogs = () => {
        this.activeDialog.value = null;
    }

    public handle = (event: Command) => {

        switch (event.type) {
            case OpenLoginDialog:
                // this.activeDialog.set(this.dialogs.loginDialog());
                return;

            case "OpenSignUpDialog":
                // this.openSignUpDialog();
                return;

            case CloseDialog:
                this.closeDialogs();
                return;
        }
    }
}

export default DialogsStore;