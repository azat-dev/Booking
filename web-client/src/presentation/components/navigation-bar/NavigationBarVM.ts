import ProfileButtonVM from "../profile-button/ProfileButtonVM";
import {ReadonlySubject} from "../../utils/binding/Subject";


class NavigationBarVM {

    public constructor(
        public readonly profileButton: ReadonlySubject<ProfileButtonVM>,
        public readonly showHostingButton: ReadonlySubject<boolean>
    ) {
    }
}

export default NavigationBarVM;
