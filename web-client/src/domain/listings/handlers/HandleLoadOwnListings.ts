import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import LoadOwnListings from "../commands/LoadOwnListings";
import LoadedOwnListings from "../events/LoadedOwnListings";
import FailedToLoadOwnListings from "../events/FailedToLoadOwnListings";
import {QueriesPrivateApi} from "../../../data/api/listings";

class HandleLoadOwnListings extends Handler {

    public constructor(
        private readonly queriesApi: QueriesPrivateApi,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: LoadOwnListings): Promise<void> => {

        try {
            const response = await this.queriesApi.getOwnListings();
            this.bus.publish(new LoadedOwnListings(
                response
            ).withSender(command.senderId));
        } catch (error: any) {
            this.bus.publish(new FailedToLoadOwnListings(
                error,
            ).withSender(command.senderId));
        }

    }
}

export default HandleLoadOwnListings;