import Avatar from "../auth/values/Avatar";
import AccommodationId from "./AccommodationId";

export class Title {
    public constructor(public readonly value: string) {}
}

export class Location {
    public constructor(
        public readonly country: string,
        public readonly city: string
    ) {}
}

export class Owner {
    public constructor(
        public readonly name: string,
        public readonly avatar: Avatar
    ) {}
}

export class Description {
    public constructor(public readonly value: string) {}
}

export class Photo {
    public constructor(
        public readonly url: string,
        public readonly description?: string
    ) {}
}

export class RoomInfo {
    public constructor(
        public readonly quantity: number,
        public readonly title: string
    ) {}
}

export enum AccommodationType {
    House = "House",
    Apartment = "Apartment",
    Hotel = "Hotel",
}

export default class Accommodation {
    public constructor(
        public readonly id: AccommodationId,
        public readonly title: Title,
        public readonly type: AccommodationType,
        public readonly location: Location,
        public readonly description: Description,
        public readonly host: Owner,
        public readonly photos: Photo[],
        public readonly numberOfGuests: number,
        public readonly rooms: RoomInfo[],
        public readonly rating?: number
    ) {}
}
