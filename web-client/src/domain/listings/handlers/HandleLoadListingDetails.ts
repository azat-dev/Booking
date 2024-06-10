import Handler from "../../utils/Handler.ts";
import Bus from "../../utils/Bus.ts";
import LoadListingDetails from "../commands/LoadListingDetails.ts";
import ListingDetailsLoaded from "../events/ListingDetailsLoaded.ts";
import FailedToLoadListingDetails from "../events/FailedToLoadListingDetails.ts";
import {QueriesPrivateApi} from "../../../data/api/listings";
import ProcessingLoadListingDetails from "../events/ProcessingLoadListingDetails.ts";


class HandleLoadListingDetails extends Handler {

    public constructor(
        private queriesApi: QueriesPrivateApi,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoadListingDetails): Promise<void> => {

        try {

            this.bus.publish(new ProcessingLoadListingDetails(command.listingId)
                .withSender(command.senderId));

            const response = await this.queriesApi.getListingPrivateDetails(
                {
                    listingId: command.listingId.val
                }
            );

            this.bus.publish(new ListingDetailsLoaded(command.listingId, response.listing)
                .withSender(command.senderId));

        } catch (error) {
            this.bus.publish(new FailedToLoadListingDetails(command.listingId, error)
                .withSender(command.senderId));
        }
    }
}

export default HandleLoadListingDetails;