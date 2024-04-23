import Accommodation, {
    AccommodationType,
} from "../../../domain/accommodations/Accommodation";
import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import PhotosGroupViewModel from "./photos-group/PhotosGroupViewModel";

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

class PageAccomodiationDetailsViewModel {
    public readonly navigationBar: NavigationBarViewModel;
    public readonly photosGroup: PhotosGroupViewModel;

    public readonly title: string;
    public readonly description: string;
    public readonly location: string;
    public readonly host: string;
    public readonly hostPhoto: string | undefined;

    public constructor(
        accommodation: Accommodation,
        onLogin: () => void,
        onSignUp: () => void
    ) {
        this.navigationBar = new NavigationBarViewModel(onLogin, onSignUp);
        this.photosGroup = new PhotosGroupViewModel(accommodation.photos);

        this.title = accommodation.title.value;
        this.description = accommodation.description.value;
        this.location = `${accommodation.type} ${accommodation.location.city}, ${accommodation.location.country}`;
        this.host = accommodation.host.name;
        this.hostPhoto = accommodation.host.avatar?.value;
    }
}

export default PageAccomodiationDetailsViewModel;
