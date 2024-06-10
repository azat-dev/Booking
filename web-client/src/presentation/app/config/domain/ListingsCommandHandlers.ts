import Bus from "../../../../domain/utils/Bus.ts";
import Handler from "../../../../domain/utils/Handler.ts";
import Command from "../../../../domain/utils/Command.ts";
import HandleLoadOwnListings from "../../../../domain/listings/handlers/HandleLoadOwnListings.ts";
import LoadOwnListings from "../../../../domain/listings/commands/LoadOwnListings.ts";
import {CommandsListingsPhotoApi, CommandsModificationsApi, QueriesPrivateApi} from "../../../../data/api/listings";
import UpdateListingDetails from "../../../../domain/listings/commands/UpdateListingDetails.ts";
import HandleUpdateListingDetails from "../../../../domain/listings/handlers/HandleUpdateListingDetails.ts";
import HandleCreateDraftListing from "../../../../domain/listings/handlers/HandleCreateDraftListing.ts";
import CommandHandlersProvider from "./CommandHandlersProvider.ts";
import CreateDraftListing from "../../../../domain/listings/commands/CreateDraftListing.ts";
import AddNewPhotoToListing from "../../../../domain/listings/commands/AddNewPhotoToListing.ts";
import HandleAddNewPhotoToListing from "../../../../domain/listings/handlers/HandleAddNewPhotoToListing.ts";
import LoadListingDetails from "../../../../domain/listings/commands/LoadListingDetails.ts";
import HandleLoadListingDetails from "../../../../domain/listings/handlers/HandleLoadListingDetails.ts";

class ListingsCommandHandlers implements CommandHandlersProvider {

    private readonly handlersByCommands: Record<string, Handler>;

    public constructor(
        listingQueriesPrivateApi: QueriesPrivateApi,
        listingsModificationsApi: CommandsModificationsApi,
        photoApi: CommandsListingsPhotoApi,
        bus: Bus,
    ) {
        this.handlersByCommands = {
            [LoadOwnListings.name]: new HandleLoadOwnListings(listingQueriesPrivateApi, bus),
            [UpdateListingDetails.name]: new HandleUpdateListingDetails(listingsModificationsApi, bus),
            [CreateDraftListing.name]: new HandleCreateDraftListing(listingsModificationsApi, bus),
            [AddNewPhotoToListing.name]: new HandleAddNewPhotoToListing(bus, photoApi),
            [LoadListingDetails.name]: new HandleLoadListingDetails(listingQueriesPrivateApi, bus),
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlersByCommands[command.constructor.name];
    }
}

export default ListingsCommandHandlers;