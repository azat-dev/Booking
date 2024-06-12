import type {Emit} from "../../utils/Bus";
import FailedCreateDraftListing from "../events/FailedCreateDraftListing";
import {CommandsModificationsApi} from "../../../data/api/listings";
import ListingId from "../values/ListingId.ts";
import ListingDraftCreated from "../events/ListingDraftCreated.ts";

class CreateDraftListing {

    public constructor(
        private readonly modificationsApi: CommandsModificationsApi,
        private readonly emit: Emit
    ) {
    }

    public execute = async (title: string): Promise<ListingId> => {

        try {
            const response = await this.modificationsApi.addListing(
                {
                    addListingRequestBody: {
                        title: title,
                        operationId: crypto.randomUUID()
                    }
                }
            );

            this.emit(
                new ListingDraftCreated(
                    new ListingId(response.listingId)
                )
            );

            return new ListingId(response.listingId);
        } catch (error: any) {
            const message = new FailedCreateDraftListing(error);
            this.emit(message);
            throw message;
        }
    }
}

export default CreateDraftListing;