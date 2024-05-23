import FullName from "../../../../../domain/auth/values/FullName";
import Subject, {ReadonlySubject} from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";
import PhotoPath from "../../../../../domain/auth/values/PhotoPath";
import Disposables from "../../../../utils/binding/Disposables";
import KeepType from "../../../../../domain/utils/KeepType.ts";

class AvatarButtonVM extends KeepType {
    public fullName: Subject<string>;
    public shortName: Subject<string>;
    public photoUrl: Subject<string | null>;

    private readonly disposables = new Disposables();

    public constructor(fullName: ReadonlySubject<FullName>, photo: ReadonlySubject<PhotoPath | null>) {

        super();
        this.fullName = value(fullName.value.toString());
        this.shortName = value(fullName.value.getInitials());
        this.photoUrl = value(photo.value?.url ?? null);

        this.disposables.addItems(
            fullName.listen((fullName) => {
                this.fullName.set(fullName.toString());
                this.shortName.set(fullName.getInitials());
            }),

            photo.listen((photo) => {
                this.photoUrl.set(photo?.url ?? null);
            })
        );
    }

    public dispose = () => {
        this.disposables.dispose();
    }
}

export default AvatarButtonVM;