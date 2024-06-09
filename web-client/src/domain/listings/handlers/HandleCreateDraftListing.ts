import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import CreateDraftListing from "../commands/CreateDraftListing";
import CreatedDraftListing from "../events/CreatedDraftListing";
import FailedCreateDraftListing from "../events/FailedCreateDraftListing";
import {CommandsModificationsApi} from "../../../data/api/listings";
import ListingId from "../values/ListingId.ts";

class HandleCreateDraftListing extends Handler {

    public constructor(
        private readonly modificationsApi: CommandsModificationsApi,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: CreateDraftListing): Promise<void> => {

        try {

            const response = await this.modificationsApi.addListing(
                {
                    addListingRequestBody: {
                        title: command.title,
                        operationId: crypto.randomUUID()
                    }
                }
            );

            this.bus.publish(
                new CreatedDraftListing(
                    new ListingId(response.listingId),
                    command.title
                )
            );
        } catch (error: any) {
            this.bus.publish(new FailedCreateDraftListing(
                error,
                command.id
            ));
        }
    }
}

export default HandleCreateDraftListing;