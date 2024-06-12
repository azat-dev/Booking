import Bus from "../../../../domain/utils/Bus.ts";
import LoadOwnListings from "../../../../domain/listings/commands/LoadOwnListings.ts";
import {CommandsListingsPhotoApi, CommandsModificationsApi, QueriesPrivateApi} from "../../../../data/api/listings";
import UpdateListingDetails, {
    UpdateListingDetailsPayload
} from "../../../../domain/listings/commands/UpdateListingDetails.ts";
import AddNewPhotoToListing from "../../../../domain/listings/commands/AddNewPhotoToListing.ts";
import LoadListingDetails from "../../../../domain/listings/commands/LoadListingDetails.ts";
import CreateDraftListing from "../../../../domain/listings/commands/CreateDraftListing.ts";
import ListingId from "../../../../domain/listings/values/ListingId.ts";

class ListingsCommands {

    public constructor(
        private readonly listingQueriesPrivateApi: QueriesPrivateApi,
        private readonly listingsModificationsApi: CommandsModificationsApi,
        private readonly photoApi: CommandsListingsPhotoApi,
        private readonly bus: Bus,
    ) {
    }

    public loadOwnListings = () => {
        return new LoadOwnListings(this.listingQueriesPrivateApi, this.bus.publish)
            .execute();
    }

    public updateListingDetails = (id: ListingId, payload: UpdateListingDetailsPayload) => {
        return new UpdateListingDetails(this.listingsModificationsApi, this.bus.publish)
            .execute(id, payload);
    }

    public createDraftListing = (title: string) => {
        return new CreateDraftListing(this.listingsModificationsApi, this.bus.publish)
            .execute(title);
    }

    public addNewPhotoToListing = (listingId: ListingId, file: File) => {
        return new AddNewPhotoToListing(this.photoApi, this.bus.publish)
            .execute(listingId, file);
    }

    public loadListingDetails = (id: ListingId) => {
        return new LoadListingDetails(this.listingQueriesPrivateApi, this.bus.publish)
            .execute(id);
    }
}

export default ListingsCommands;