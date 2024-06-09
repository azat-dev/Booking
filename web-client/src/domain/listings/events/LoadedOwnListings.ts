import AppEvent from "../../utils/AppEvent";

class LoadedOwnListings extends AppEvent {

    public constructor(
        public readonly listings: any[]
    ) {
        super();
    }
}

export default LoadedOwnListings;