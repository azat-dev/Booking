import TabsVM from "./tabs/TabsVM.ts";
import VM from "../../utils/VM.ts";
import ProfileButtonAuthenticatedVM
    from "../profile-button/profile-button-authenticated/ProfileButtonAuthenticatedVM.ts";

class HostingNavigationBarVM extends VM {

    public readonly tabs: TabsVM;

    public constructor(
        public readonly profileButton: ProfileButtonAuthenticatedVM
    ) {

        super();
        this.tabs = new TabsVM();
    }
}

export default HostingNavigationBarVM;
