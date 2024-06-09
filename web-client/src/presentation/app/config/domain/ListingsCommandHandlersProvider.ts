import Bus from "../../../../domain/utils/Bus.ts";
import Handler from "../../../../domain/utils/Handler.ts";
import Command from "../../../../domain/utils/Command.ts";
import HandleLoadOwnListings from "../../../../domain/listings/handlers/HandleLoadOwnListings.ts";
import LoadOwnListings from "../../../../domain/listings/commands/LoadOwnListings.ts";
import {CommandsModificationsApi, QueriesPrivateApi} from "../../../../data/api/listings";
import UpdateListingDetails from "../../../../domain/listings/commands/UpdateListingDetails.ts";
import HandleUpdateListingDetails from "../../../../domain/listings/handlers/HandleUpdateListingDetails.ts";
import HandleCreateDraftListing from "../../../../domain/listings/handlers/HandleCreateDraftListing.ts";
import CommandHandlersProvider from "./CommandHandlersProvider.ts";
import CreateDraftListing from "../../../../domain/listings/commands/CreateDraftListing.ts";

class ListingsCommandHandlersProvider implements CommandHandlersProvider {

    private readonly handlersByCommands: Record<string, Handler>;

    public constructor(
        listingQueriesPrivateApi: QueriesPrivateApi,
        listingsModificationsApi: CommandsModificationsApi,
        bus: Bus,
    ) {
        this.handlersByCommands = {
            [LoadOwnListings.name]: new HandleLoadOwnListings(listingQueriesPrivateApi, bus),
            [UpdateListingDetails.name]: new HandleUpdateListingDetails(listingsModificationsApi, bus),
            [CreateDraftListing.name]: new HandleCreateDraftListing(listingsModificationsApi, bus)
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        const handler = this.handlersByCommands[command.constructor.name];
        console.log("LISTING", handler, typeof command.constructor.name, this.handlersByCommands);

        return handler;
    }
}

export default ListingsCommandHandlersProvider;