import ListingsTableVM from "./table/ListingsTableVM.ts";
import HostingNavigationBarVM from "../../components/hosting-navigation-bar/HostingNavigationBarVM.ts";

class PageListingsVM {

    public constructor(
        public readonly listingsTable: ListingsTableVM,
        public readonly navigationBar: HostingNavigationBarVM,
        public readonly addNewListing: () => void
    ) {
    }
}

export default PageListingsVM;