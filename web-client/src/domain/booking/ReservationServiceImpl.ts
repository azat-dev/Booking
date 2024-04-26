import AccommodationId from "../accommodations/AccommodationId";
import ReservationService from "./ReservationService";
import Cost from "./values/Cost";
import DatesRange from "./values/DatesRange";
import GuestsQuantity from "./values/GuestsQuantity";
import Price from "./values/Price";

class ReservationServiceImpl implements ReservationService {
    public getAccommodationCost = async (
        accommodationId: AccommodationId,
        dates: DatesRange,
        guestsCount: GuestsQuantity
    ) => {
        return new Cost(
            new Price(100, "$"),
            new Price(80, "$"),
            new Price(20, "$")
        );
    };
}

export default ReservationServiceImpl;
