import Subject from "../../utils/binding/Subject.ts";
import {DialogVM} from "../AppVM.tsx";

export default interface PropsActiveDialog {
    vm: Subject<DialogVM | null>;
    views: Record<string, React.ComponentType<any>>
}