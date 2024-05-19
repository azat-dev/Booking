import Subject from "../../utils/binding/Subject";

export interface DialogItem {
    type: string;
    vm?: any;
}

class ActiveDialogVM {
    public constructor(public readonly dialog: Subject<DialogItem | null>) {
    }
}

export default ActiveDialogVM;