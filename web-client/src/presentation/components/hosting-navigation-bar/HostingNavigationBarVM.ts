import {ReadonlySubject} from "../../utils/binding/Subject";
import TabsVM from "./tabs/TabsVM.ts";
import ProfileButtonVM from "../profile-button/ProfileButtonVM.ts";
import VM from "../../utils/VM.ts";

class HostingNavigationBarVM extends VM {

    public readonly tabs: TabsVM;
    public constructor(
        public readonly profileButton: ReadonlySubject<ProfileButtonVM>
    ) {

        super();
        this.tabs = new TabsVM();
    }
}

export default HostingNavigationBarVM;
