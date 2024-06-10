import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import ListingId from "../../../../../domain/listings/values/ListingId.ts";
import CreateDraftListing from "../../../../../domain/listings/commands/CreateDraftListing.ts";
import FailedCreateDraftListing from "../../../../../domain/listings/events/FailedCreateDraftListing.ts";
import UpdateListingDetails from "../../../../../domain/listings/commands/UpdateListingDetails.ts";
import ListingDetailsUpdated from "../../../../../domain/listings/events/ListingDetailsUpdated.ts";
import FailedUpdateListingDetails from "../../../../../domain/listings/events/FailedUpdateListingDetails.ts";
import HostingComponentsConfig from "./HostingComponentsConfig.ts";
import LoadOwnListings from "../../../../../domain/listings/commands/LoadOwnListings.ts";
import LoadedOwnListings from "../../../../../domain/listings/events/LoadedOwnListings.ts";
import ListingDraftCreated from "../../../../../domain/listings/events/ListingDraftCreated.ts";
import NewPhotoAddedToListing from "../../../../../domain/listings/events/NewPhotoAddedToListing.ts";
import FailedToAddNewPhotoToListing from "../../../../../domain/listings/events/FailedToAddNewPhotoToListing.ts";
import OpenAddPhotoToListingDialog from "../../../../hosting/commands/OpenAddPhotoToListingDialog.ts";
import LoadListingDetails from "../../../../../domain/listings/commands/LoadListingDetails.ts";
import ListingDetailsLoaded from "../../../../../domain/listings/events/ListingDetailsLoaded.ts";

class HostingPagesVmConfig {
    public constructor(
        private readonly components: HostingComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public listingsPage = async (session: AppSessionAuthenticated) => {

        const PageListingsVM = (await import("../../../../hosting/page-listings/PageListingsVM.ts")).default;
        const ListingsTableVM = (await import("../../../../hosting/page-listings/table/ListingsTableVM.ts")).default;

        const navigationBar = await this.components.hostingNavigationBar();

        const table = new ListingsTableVM();

        table.listenOwnEvents(this.bus, event => {
            if (event instanceof LoadedOwnListings) {
                table.displayLoadedListings(event.listings);
                return;
            }

            if (event instanceof FailedCreateDraftListing) {
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

        return vm;
    }

    public editListingPage = async (session: AppSessionAuthenticated) => {

        const PageEditListingVM = (await import("../../../../hosting/page-edit-listing/PageEditListingVM.ts")).default;


        const vm = new PageEditListingVM(
            null
        );

        vm.listenOwnEvents(this.bus, response => {
            if (response instanceof ListingDraftCreated) {
                vm.displayCreatedDraft(response.listingId);
                return
            }

            if (response instanceof FailedCreateDraftListing) {
                vm.displayFailedCreateDraft();
                return
            }

            if (response instanceof ListingDetailsUpdated) {
                vm.displayUpdatedDescription();
                return
            }

            if (response instanceof FailedUpdateListingDetails) {
                vm.displayFailedUpdateDraft();
                return
            }
        });

        vm.listenOwnEvents(this.bus, response => {

            if (response instanceof NewPhotoAddedToListing) {
                vm.displayAddedNewPhoto();
                return;
            }

            if (response instanceof FailedToAddNewPhotoToListing) {
                vm.displayFailedFailedToAddPhoto();
                return;
            }
        });

        vm.listenOwnEvents(this.bus, response => {
            if (response instanceof ListingDetailsLoaded) {
                vm.displayLoadedListingDetails(response.details);
                return;
            }
        });

        vm.delegate = {
            createDraft: (title: string) => {
                this.bus.publish(
                    new CreateDraftListing(title)
                        .withSender(vm)
                );
            },
            updateDescription: (listingId: ListingId, description: string) => {

                this.bus.publish(
                    new UpdateListingDetails(
                        listingId,
                        {description}
                    ).withSender(vm)
                );
            },
            addNewPhoto: (listingId: ListingId) => {
                this.bus.publish(
                    new OpenAddPhotoToListingDialog(listingId)
                        .withSender(vm)
                )
            },
            loadPhotos: (listingId: ListingId) => {
                this.bus.publish(new LoadListingDetails(listingId)
                    .withSender(vm));
            }
        }

        return vm;
    }
}

export default HostingPagesVmConfig;