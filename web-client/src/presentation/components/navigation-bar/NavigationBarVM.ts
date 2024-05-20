import ProfileButtonVM from "./profile-button/ProfileButtonVM";
import Subject from "../../utils/binding/Subject";


class NavigationBarVM {

    public constructor(
        public readonly profileButton: Subject<ProfileButtonVM>
    ) {
    }
}

export default NavigationBarVM;
