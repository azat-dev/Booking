import CommandHandlersProvider from "../../domain/CommandHandlersProvider.ts";
import OpenedAddPhotoToListingDialog from "../../../../hosting/events/OpenedAddPhotoToListingDialog.ts";
import HandleAddNewPhotoToListing from "../../../../../domain/listings/handlers/HandleAddNewPhotoToListing.ts";
import {CommandsListingsPhotoApi} from "../../../../../data/api/listings";
import Handler from "../../../../../domain/utils/Handler.ts";
import OpenAddPhotoToListingDialog from "../../../../hosting/commands/OpenAddPhotoToListingDialog.ts";
import HandleOpenAddPhotoToListingDialog from "../../../../hosting/handlers/HandleOpenAddPhotoToListingDialog.ts";
import Command from "../../../../../domain/utils/Command.ts";
import Bus from "../../../../../domain/utils/Bus.ts";

class HostingCommandsHandlers implements CommandHandlersProvider {

    private readonly handlers: Record<string, Handler>;

    public constructor(
        bus: Bus
    ) {

        this.handlers = {
            [OpenAddPhotoToListingDialog.name]: new HandleOpenAddPhotoToListingDialog(bus),
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlers[command.constructor.name];
    }
}

export default HostingCommandsHandlers;