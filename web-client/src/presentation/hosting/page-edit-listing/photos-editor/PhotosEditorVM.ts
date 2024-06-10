import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";
import InitialPhotoInputVM from "./initial-photo-input/InitialPhotoInputVM.ts";
import ListingId from "../../../../domain/listings/values/ListingId.ts";
import {ListingPhotoPath} from "../../../../data/api/listings";

class PhotosEditorVM {

    public initialPhotoInput: Subject<InitialPhotoInputVM | null>;
    public canMoveNext: Subject<boolean>;

    public photos: Subject<ListingPhotoPath[]>;

    public constructor(
        private listingId: ListingId,
        private readonly addNewPhoto: (listingId: ListingId) => void,
        private readonly loadPhotos: (listingId: ListingId) => void
    ) {
        this.initialPhotoInput = value(null);
        this.photos = value([]);
        this.canMoveNext = value(false);
        this.updateCanMoveNext();
        this.displayPhotos([]);
    }

    public load = () => {
        this.loadPhotos(this.listingId);
    }

    public displayFailedToAddNewPhoto = () => {

    }

    public displayAddedNewPhoto = () => {
        this.loadPhotos(this.listingId);
    }

    public displayPhotos = (photos: ListingPhotoPath[]) => {

        if (photos.length) {
            this.initialPhotoInput.set(null);
        } else {
            this.initialPhotoInput.set(
                new InitialPhotoInputVM(() => this.addNewPhoto(this.listingId))
            );
        }

        this.photos.set(photos);
        this.updateCanMoveNext();
    }

    private updateCanMoveNext = () => {
        this.canMoveNext.set(false);
    }
}

export default PhotosEditorVM;