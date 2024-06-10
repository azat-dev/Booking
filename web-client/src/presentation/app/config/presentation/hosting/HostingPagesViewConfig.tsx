import HostingPagesVmConfig from "./HostingPagesVmConfig.ts";
import PageEditListing from "../../../../hosting/page-edit-listing/PageEditListing.tsx";
import PageListings from "../../../../hosting/page-listings/PageListings.tsx";
import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";

class HostingPagesViewConfig {

    public constructor(private readonly vm: HostingPagesVmConfig) {
    }

    public editListingPage = async (session: AppSessionAuthenticated) => {

        return (
            <PageEditListing
                vm={await this.vm.editListingPage(session)}
            />
        )
    }

    public listingsPage = async (session: AppSessionAuthenticated) => {
        return (
            <PageListings
                vm={await this.vm.listingsPage(session)}
            />
        )
    }
}

export default HostingPagesViewConfig;