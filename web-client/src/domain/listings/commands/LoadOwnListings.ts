import type {Emit} from "../../utils/Bus";
import LoadedOwnListings from "../events/LoadedOwnListings";
import FailedToLoadOwnListings from "../events/FailedToLoadOwnListings";
import {ListingPrivateDetails, QueriesPrivateApi} from "../../../data/api/listings";

class LoadOwnListings {

    public constructor(
        private readonly queriesApi: QueriesPrivateApi,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<ListingPrivateDetails[]> => {

        try {
            const response = await this.queriesApi.getOwnListings();
            this.emit(new LoadedOwnListings(response));
            return response;
        } catch (error: any) {
            const errorMessage = new FailedToLoadOwnListings(error)
            this.emit(errorMessage);
            throw errorMessage;
        }
    }
}

export default LoadOwnListings;