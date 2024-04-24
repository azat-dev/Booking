import Accommodation from "./Accommodation";
import AccommodationId from "./AccommodationId";

export default interface AccommodationsRegistry {
    getAccommodationById(id: AccommodationId): Promise<Accommodation>;
}
