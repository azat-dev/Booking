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
        private readonly addNewPhoto: (listingId: ListingId) => Promise<void>,
        private readonly loadPhotos: (listingId: ListingId) => Promise<ListingPhotoPath[]>
    ) {
        this.initialPhotoInput = value(null);
        this.photos = value([]);
        this.canMoveNext = value(false);
        this.updateCanMoveNext();
        this.displayPhotos([]);
    }

    public load = async () => {
        try {
            const loadedPhotos = await this.loadPhotos(this.listingId);
            this.displayPhotos(loadedPhotos);
        } catch (e) {
        }
    }

    private addNewPhotoToListing = async () => {
        await this.addNewPhoto(this.listingId);
        this.load();
    }

    private displayPhotos = (photos: ListingPhotoPath[]) => {

        if (photos.length) {
            this.initialPhotoInput.set(null);
        } else {
            this.initialPhotoInput.set(new InitialPhotoInputVM(this.addNewPhotoToListing));
        }

        this.photos.set(photos);
        this.updateCanMoveNext();
    }

    private updateCanMoveNext = () => {
        this.canMoveNext.set(false);
    }
}

export default PhotosEditorVM;