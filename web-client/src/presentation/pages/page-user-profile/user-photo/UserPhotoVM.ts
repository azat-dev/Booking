import Subject, {ReadonlySubject} from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import FullName from "../../../../domain/auth/values/FullName";
import PhotoPath from "../../../../domain/auth/values/PhotoPath";
import mappedValue from "../../../utils/binding/mappedValue.ts";
import VM from "../../../utils/VM.ts";

class UserPhotoVM extends VM {

    public readonly initials: ReadonlySubject<string>;
    public readonly photo: ReadonlySubject<string | undefined>;
    public isUploading: Subject<boolean>;

    public constructor(
        fullName: ReadonlySubject<FullName>,
        photo: ReadonlySubject<PhotoPath | null>,
        private readonly _openUploadDialog: () => void
    ) {
        super();
        this.isUploading = value(false);
        this.photo = mappedValue(photo, v => v?.url ?? undefined);
        this.initials = mappedValue(fullName, v => v.getInitials());

        this.cleanOnDestroy(this.photo, this.initials);
    }

    public didCompleteUpload = () => {
        this.isUploading.set(false);
    }

    public openUploadDialog = async () => {
        this._openUploadDialog();
    }

    public displayUploading = () => {
        this.isUploading.set(true);
    }

    public displayUploaded = () => {
        this.isUploading.set(false);
    }

    public displayFailedUpload = () => {
        this.isUploading.set(false);
    }
}

export default UserPhotoVM;