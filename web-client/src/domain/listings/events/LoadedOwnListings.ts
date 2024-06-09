import AppEvent from "../../utils/AppEvent";

class LoadedOwnListings extends AppEvent {

    public constructor(
        public readonly operationId: string,
        public readonly listings: any[] // TODO: Define the type of the listings array here
    ) {
        super();
    }
}

export default LoadedOwnListings;