import Accommodation, {
    AccommodationType,
    Owner,
    RoomInfo,
    Title,
    Location,
    Description,
    Photo,
} from "./Accommodation";
import Avatar from "../auth/values/Avatar";
import AccommodationsRegistry from "./AccommodationsRegistry";
import AccommodationId from "./AccommodationId";

class AccommodationsRegistryImpl implements AccommodationsRegistry {
    public getAccommodationById = async (id: AccommodationId) => {
        const accommodation = new Accommodation(
            new Title("Small House"),
            AccommodationType.House,
            new Location("USA", "New York"),
            new Description(
                "This is a small house in the middle of the city. It has a small garden and a garage. It is perfect for a small family. It is close to the city center. It has a small kitchen and a living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. "
            ),
            new Owner("John Doe", new Avatar("https://example.com/avatar.jpg")),
            [
                new Photo("https://picsum.photos/id/11/200/300"),
                new Photo("https://picsum.photos/id/12/200/300"),
                new Photo("https://picsum.photos/id/255/200/300"),
            ],
            2,
            [
                new RoomInfo(1, "Bedroom"),
                new RoomInfo(1, "Living room"),
                new RoomInfo(1, "Kitchen"),
                new RoomInfo(1, "Bathroom"),
            ],
            4.5
        );

        await new Promise((resolve) => setTimeout(resolve, 1000));
        return accommodation;
    };
}

export default AccommodationsRegistryImpl;
