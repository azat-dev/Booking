import AccommodationId from "../accommodations/AccommodationId";
import Cost from "./values/Cost";
import DatesRange from "./values/DatesRange";
import GuestsQuantity from "./values/GuestsQuantity";

interface ReservationService {
    getAccommodationCost(
        accommodationId: AccommodationId,
        dates: DatesRange,
        guestsCount: GuestsQuantity
    ): Promise<Cost>;
}

export default ReservationService;
