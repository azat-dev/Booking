import Subject, {ReadonlySubject} from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import { fileDialog } from "file-select-dialog";
import fullName from "../../../../domain/auth/values/FullName";
import FullName from "../../../../domain/auth/values/FullName";
import {PhotoPath} from "../../../../domain/auth/values/PhotoPath";
import Disposables from "../../../utils/binding/Disposables";

class UserPhotoVM {

    public readonly initials: Subject<string>;
    public readonly photo: Subject<string | undefined>;
    public isUploading: Subject<boolean>;

    private disposables = new Disposables();

    public constructor(
        fullName: ReadonlySubject<FullName>,
        photo: ReadonlySubject<PhotoPath | null>,
        private uploadPhoto: (file: File) => Promise<void>
    ) {

        this.isUploading = value(false);
        this.photo = value(photo.value?.url ?? undefined);
        this.initials = value(fullName.value.getInitials());

        this.disposables.addItems(

            fullName.listen((newFullName) => {
                this.initials.set(newFullName.getInitials());
            }),

            photo.listen((newPhoto) => {
                this.photo.set(newPhoto?.url ?? undefined);
            })
        )
    }

    public updatePhoto = (newPhoto: PhotoPath | null) => {
        this.photo.set(newPhoto?.url ?? undefined);
        this.isUploading.set(false);
    }

    public updateFullName = (newFullName: FullName) => {
        this.initials.set(newFullName.getInitials());
    }

    public openUploadDialog = async () => {

        const files = await fileDialog({ accept: ['.png', '.jpg', '.webp', '.jpeg'] });
        if (files.length === 0) {
            return;
        }

        let file = files[0];

        this.isUploading.set(true);
        try {
            await this.uploadPhoto(file);
        } catch (error) {
            console.error(error);
        }
        this.isUploading.set(false);
    }

    public dispose = () => {
        this.disposables.dispose();
    }
}

export default UserPhotoVM;