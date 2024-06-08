import ProfileButtonVM from "./ProfileButtonVM.ts";
import {ReadonlySubject} from "../../utils/binding/Subject.ts";

interface PropsProfileButton {
    vm: ReadonlySubject<ProfileButtonVM>;
}

export default PropsProfileButton;
