import HostingPagesVmConfig from "./HostingPagesVmConfig.ts";
import PageEditListing from "../../../../hosting/page-edit-listing/PageEditListing.tsx";
import PageListings from "../../../../hosting/page-listings/PageListings.tsx";
import ListingId from "../../../../../domain/listings/values/ListingId.ts";

class HostingPagesViewConfig {

    public constructor(private readonly vm: HostingPagesVmConfig) {
    }

    public editListingPage = async (
        listingId: string | null,
        initialStep: string | null,
        updateParams: (step: string | null, listingId: string | null) => void
    ) => {

        return (
            <PageEditListing
                vm={await this.vm.editListingPage(listingId, initialStep, updateParams)}
            />
        )
    }

    public listingsPage = async (
        openListingPage: (id: ListingId) => void,
        addNewListing: () => void
    ) => {
        const vm = await this.vm.listingsPage(openListingPage, addNewListing);
        return (
            <PageListings
                vm={vm}
            />
        )
    }
}

export default HostingPagesViewConfig;