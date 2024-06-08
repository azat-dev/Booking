import ProfileButtonVM from "./profile-button/ProfileButtonVM";
import {ReadonlySubject} from "../../utils/binding/Subject";
import TabsVM from "./tabs/TabsVM.ts";

class HostingNavigationBarVM {

    public readonly tabs: TabsVM;
    public constructor(
        public readonly profileButton: ReadonlySubject<ProfileButtonVM>
    ) {

        this.tabs = new TabsVM();
    }
}

export default HostingNavigationBarVM;
