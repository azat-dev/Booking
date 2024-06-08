import Listings from "../../../domain/listings/entities/Listings.ts";
import ListingsTableVM from "./table/ListingsTableVM.ts";
import HostingNavigationBarVM from "../../components/hosting-navigation-bar/HostingNavigationBarVM.ts";

class PageListingsVM {

    public readonly listingsTable: ListingsTableVM;

    public constructor(
        private readonly listings: Listings,
        public readonly navigationBar: HostingNavigationBarVM
    ) {

        this.listingsTable = new ListingsTableVM(listings);

    }
}

export default PageListingsVM;