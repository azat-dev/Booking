import Subject, {ReadonlySubject} from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import FullName from "../../../../domain/auth/values/FullName";
import PhotoPath from "../../../../domain/auth/values/PhotoPath";
import Disposables from "../../../utils/binding/Disposables";
import mappedValue from "../../../utils/binding/mappedValue.ts";

class UserPhotoVM {

    public readonly initials: ReadonlySubject<string>;
    public readonly photo: ReadonlySubject<string | undefined>;
    public isUploading: Subject<boolean>;

    private disposables = new Disposables();

    public constructor(
        fullName: ReadonlySubject<FullName>,
        photo: ReadonlySubject<PhotoPath | null>,
        private readonly updloadUserPhoto: () => Promise<void>,
    ) {

        this.isUploading = value(false);
        this.photo = mappedValue(photo, v => v?.url ?? undefined);
        this.initials = mappedValue(fullName, v => v.getInitials());

        this.disposables.addItems(
            this.photo,
            this.initials
        )
    }

    public didCompleteUpload = () => {
        this.isUploading.set(false);
    }

    public openUploadDialog = async () => {

        this.isUploading.set(true);

        try {
            await this.updloadUserPhoto();
        } finally {
            this.isUploading.set(false);
        }

    }

    public dispose = () => {
        this.disposables.dispose();
    }
}

export default UserPhotoVM;