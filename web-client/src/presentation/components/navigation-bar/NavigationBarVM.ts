import ProfileButtonVM from "../profile-button/ProfileButtonVM";
import {ReadonlySubject} from "../../utils/binding/Subject";
import VM from "../../utils/VM.ts";


class NavigationBarVM extends VM {

    public constructor(
        public readonly profileButton: ReadonlySubject<ProfileButtonVM>,
        public readonly showHostingButton: ReadonlySubject<boolean>
    ) {
        super();
    }
}

export default NavigationBarVM;
