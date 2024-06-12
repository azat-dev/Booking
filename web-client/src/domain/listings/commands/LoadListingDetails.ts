import ListingDetailsLoaded from "../events/ListingDetailsLoaded.ts";
import FailedToLoadListingDetails from "../events/FailedToLoadListingDetails.ts";
import {ListingPrivateDetails, QueriesPrivateApi} from "../../../data/api/listings";
import ProcessingLoadListingDetails from "../events/ProcessingLoadListingDetails.ts";
import ListingId from "../values/ListingId.ts";
import {Emit} from "../../utils/Bus.ts";


class LoadListingDetails {

    public constructor(
        private queriesApi: QueriesPrivateApi,
        private readonly emit: Emit
    ) {
    }

    public execute = async (listingId: ListingId): Promise<ListingPrivateDetails> => {

        try {

            this.emit(new ProcessingLoadListingDetails(listingId));

            const response = await this.queriesApi.getListingPrivateDetails(
                {
                    listingId: listingId.val
                }
            );

            this.emit(new ListingDetailsLoaded(listingId, response.listing));
            return response.listing;

        } catch (error) {
            const errMsg = new FailedToLoadListingDetails(listingId, error);
            this.emit(errMsg);
            throw errMsg;
        }
    }
}

export default LoadListingDetails;