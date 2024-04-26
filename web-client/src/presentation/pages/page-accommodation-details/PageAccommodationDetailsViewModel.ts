import Accommodation, {
    AccommodationType,
} from "../../../domain/accommodations/Accommodation";
import AccommodationId from "../../../domain/accommodations/AccommodationId";
import Cost from "../../../domain/booking/values/Cost";
import DatesRange from "../../../domain/booking/values/DatesRange";
import GuestsQuantity from "../../../domain/booking/values/GuestsQuantity";
import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import PhotosGroupViewModel from "./photos-group/PhotosGroupViewModel";
import RequestReservationCardViewModel from "./request-reservation-card/RequestReservationCardViewModel";

const accommodationTypeToText = (type: AccommodationType): string => {
    switch (type) {
        case AccommodationType.House:
            return "House";
        case AccommodationType.Apartment:
            return "Apartment";
        case AccommodationType.Hotel:
            return "Hotel";
    }
};

class PageAccommodationDetailsViewModel {
    public readonly navigationBar: NavigationBarViewModel;
    public readonly photosGroup: PhotosGroupViewModel;

    public readonly title: string;
    public readonly description: string;
    public readonly location: string;
    public readonly host: string;
    public readonly hostPhoto: string | undefined;
    public readonly roomInfo: string;
    public readonly rating: number | undefined;
    public readonly requestReservationCard: RequestReservationCardViewModel;

    public constructor(
        accommodation: Accommodation,
        onLogin: () => void,
        onSignUp: () => void,
        getCostDetails: (
            accommodation: AccommodationId,
            datesRange: DatesRange,
            guestsCount: GuestsQuantity
        ) => Promise<Cost>
    ) {
        this.navigationBar = new NavigationBarViewModel(onLogin, onSignUp);
        this.photosGroup = new PhotosGroupViewModel(accommodation.photos);

        this.title = accommodation.title.value;
        this.description = accommodation.description.value;
        this.location = `${accommodationTypeToText(accommodation.type)}, ${
            accommodation.location.city
        }, ${accommodation.location.country}`;
        this.host = accommodation.host.name;
        this.hostPhoto = accommodation.host.avatar?.value;
        this.roomInfo = accommodation.rooms
            .map((room) => `${room.quantity} ${room.title}`)
            .join(", ");

        this.rating = accommodation.rating;
        this.requestReservationCard = new RequestReservationCardViewModel(
            async (datesRange, guests) => {
                return getCostDetails(accommodation.id, datesRange, guests);
            }
        );
    }
}

export default PageAccommodationDetailsViewModel;
