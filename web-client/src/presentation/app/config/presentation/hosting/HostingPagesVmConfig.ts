import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import ListingId from "../../../../../domain/listings/values/ListingId.ts";
import HostingComponentsConfig from "./HostingComponentsConfig.ts";
import HostingCommands from "./HostingCommands.ts";
import ListingsCommands from "../../domain/ListingsCommands.ts";
import Bus from "../../../../../domain/utils/Bus.ts";

class HostingPagesVmConfig {
    public constructor(
        private readonly session: AppSessionAuthenticated,
        private readonly components: HostingComponentsConfig,
        private readonly hostingCommands: HostingCommands,
        private readonly listingCommands: ListingsCommands,
        private readonly bus: Bus
    ) {
    }

    public listingsPage = async (
        openListingPage: (id: ListingId) => void,
        addNewListing: () => void
    ) => {

        const PageListingsVM = (await import("../../../../hosting/page-listings/PageListingsVM.ts")).default;
        const ListingsTableVM = (await import("../../../../hosting/page-listings/table/ListingsTableVM.ts")).default;

        const navigationBar = await this.components.hostingNavigationBar();

        const table = new ListingsTableVM(
            this.listingCommands.loadOwnListings,
            openListingPage
        );

        return new PageListingsVM(
            table,
            navigationBar,
            addNewListing
        );
    }

    public editListingPage = async (
        listingId: string | null,
        initialStep: string | null,
        updateParams: (step: string | null, listingId: string | null) => void
    ) => {

        const PageEditListingVM = (await import("../../../../hosting/page-edit-listing/PageEditListingVM.ts")).default;

        const vm = new PageEditListingVM(
            listingId ? new ListingId(listingId) : null,
            initialStep as any,
            this.listingCommands.createDraftListing,
            this.hostingCommands.openAddPhotoToListingDialog,
            this.listingCommands.loadListingDetails,
            async (listingId, title) => {
                await this.listingCommands.updateListingDetails(listingId, {title});
            },
            async (listingId: ListingId, description: string) => {
                await this.listingCommands.updateListingDetails(listingId, {description});
            },
            async (listingId: ListingId) => {
                const details = await this.listingCommands.loadListingDetails(listingId);
                return details.photos ?? [];

            },
            updateParams
        );

        return vm;
    }
}

export default HostingPagesVmConfig;