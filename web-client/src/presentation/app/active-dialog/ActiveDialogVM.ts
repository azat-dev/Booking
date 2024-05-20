import Subject from "../../utils/binding/Subject";
import {DialogVM} from "../app-model/AppVM";

class ActiveDialogVM {
    public constructor(public readonly dialog: Subject<DialogVM | null>) {
    }
}

export default ActiveDialogVM;