import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import UpdateListingDetails from "../commands/UpdateListingDetails";
import ListingDetailsUpdated from "../events/ListingDetailsUpdated";
import FailedUpdateListingDetails from "../events/FailedUpdateListingDetails";
import {CommandsModificationsApi} from "../../../data/api/listings";

class HandleUpdateListingDetails extends Handler {

    public constructor(
        private readonly modificationsApi: CommandsModificationsApi,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: UpdateListingDetails): Promise<void> => {

        const operationId = crypto.randomUUID();

        try {

            await this.modificationsApi.updateListingDetails(
                {
                    listingId: command.listingId.val,
                    updateListingDetailsRequestBody: {
                        ...command.payload,
                        operationId
                    } as any
                }
            );

            this.bus.publish(
                new ListingDetailsUpdated(
                    command.listingId,
                    command.payload,
                    command.id
                )
            );
        } catch (error: any) {
            this.bus.publish(new FailedUpdateListingDetails(
                command.listingId,
                error,
                command.id
            ));
        }
    }
}

export default HandleUpdateListingDetails;