import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import Bus, {matchClasses} from "../../../../../domain/utils/Bus.ts";
import ListingId from "../../../../../domain/listings/values/ListingId.ts";
import CreateDraftListing from "../../../../../domain/listings/commands/CreateDraftListing.ts";
import CreatedDraftListing from "../../../../../domain/listings/events/CreatedDraftListing.ts";
import FailedCreateDraftListing from "../../../../../domain/listings/events/FailedCreateDraftListing.ts";
import UpdateListingDetails from "../../../../../domain/listings/commands/UpdateListingDetails.ts";
import ListingDetailsUpdated from "../../../../../domain/listings/events/ListingDetailsUpdated.ts";
import FailedUpdateListingDetails from "../../../../../domain/listings/events/FailedUpdateListingDetails.ts";
import HostingComponentsConfig from "./HostingComponentsConfig.ts";
import LoadOwnListings from "../../../../../domain/listings/commands/LoadOwnListings.ts";
import LoadedOwnListings from "../../../../../domain/listings/events/LoadedOwnListings.ts";

class HostingPagesConfig {
    public constructor(
        private readonly components: HostingComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public listingsPage = async (session: AppSessionAuthenticated) => {

        const PageListingsVM = (await import("../../../../pages/page-listings/PageListingsVM.ts")).default;
        const ListingsTableVM = (await import("../../../../pages/page-listings/table/ListingsTableVM.ts")).default;

        const navigationBar = await this.components.hostingNavigationBar();

        const table = new ListingsTableVM();

        table.listenOwnEvents(this.bus, event => {
            if (event instanceof LoadedOwnListings) {
                table.displayLoadedListings(event.listings);
                return;
            }

            if (event instanceof  FailedCreateDraftListing) {
                table.displayFailedLoadListings();
                return;
            }

        })

        table.delegate = {
            loadListings: () => {
                this.bus.publish(
                    new LoadOwnListings().withSender(table)
                )
            }
        }

        const vm = new PageListingsVM(
            table,
            navigationBar
        );

        table.delegate = {
            loadListings: async () => {

            }
        };

        return vm;
    }

    public editListingPage = async (session: AppSessionAuthenticated) => {

        const PageEditListingVM = (await import("../../../../pages/page-edit-listing/PageEditListingVM.ts")).default;


        const vm = new PageEditListingVM(
            null
        );

        vm.delegate = {
            createDraft: async (title: string) => {
                const response = await this.bus.publishAndWaitFor(
                    new CreateDraftListing(title),
                    matchClasses(CreatedDraftListing)
                );

                if (response instanceof CreatedDraftListing) {
                    vm.displayCreatedDraft(response.listingId);
                    return
                }

                if (response instanceof FailedCreateDraftListing) {
                    vm.displayFailedCreateDraft();
                    return
                }

                throw new Error("Unexpected response");
            },
            updateDescription: async (listingId: ListingId, description: string) => {

                const response = await this.bus.publishAndWaitFor(
                    new UpdateListingDetails(
                        listingId,
                        {description}
                    ),
                    matchClasses(CreatedDraftListing)
                );

                if (response instanceof ListingDetailsUpdated) {
                    vm.displayUpdatedDescription();
                    return
                }

                if (response instanceof FailedUpdateListingDetails) {
                    vm.displayFailedUpdateDraft();
                    return
                }

                throw new Error("Unexpected response");
            }
        }

        return vm;
    }
}

export default HostingPagesConfig;