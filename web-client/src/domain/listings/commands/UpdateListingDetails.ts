import ListingDetailsUpdated from "../events/ListingDetailsUpdated";
import FailedUpdateListingDetails from "../events/FailedUpdateListingDetails";
import {CommandsModificationsApi} from "../../../data/api/listings";
import ListingId from "../values/ListingId.ts";
import {Emit} from "../../utils/Bus.ts";

export interface UpdateListingDetailsPayload {
    title?: string;
    description?: string;
    status?: string;
}

class UpdateListingDetails {

    public constructor(
        private readonly modificationsApi: CommandsModificationsApi,
        private readonly emit: Emit
    ) {
    }

    public execute = async (
        listingId: ListingId,
        payload: UpdateListingDetailsPayload
    ): Promise<void> => {

        const operationId = crypto.randomUUID();

        try {

            await this.modificationsApi.updateListingDetails(
                {
                    listingId: listingId.val,
                    updateListingDetailsRequestBody: {
                        operationId,
                        fields: {
                            ...payload,
                        }
                    } as any
                }
            );

            this.emit(
                new ListingDetailsUpdated(
                    listingId,
                    payload
                )
            );
        } catch (error: any) {

            const message = new FailedUpdateListingDetails(
                listingId,
                error
            );

            this.emit(message);
            throw message;
        }
    }
}

export default UpdateListingDetails;