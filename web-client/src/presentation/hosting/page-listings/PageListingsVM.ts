import ListingsTableVM from "./table/ListingsTableVM.ts";
import HostingNavigationBarVM from "../../components/hosting-navigation-bar/HostingNavigationBarVM.ts";

class PageListingsVM {

    public constructor(
        public readonly listingsTable: ListingsTableVM,
        public readonly navigationBar: HostingNavigationBarVM
    ) {
    }
}

export default PageListingsVM;