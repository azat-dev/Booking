import FullName from "../../../../../domain/auth/CurrentSession/Session/FullName";
import Subject from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";
import {PhotoPath} from "../../../../../domain/auth/values/PhotoPath";

class AvatarButtonViewModel {
    public fullName: Subject<string>;
    public shortName: Subject<string>;
    public photoUrl: Subject<string | null>;

    public constructor(fullName: FullName, photo: PhotoPath | null) {
        this.fullName = value(
            `${fullName.firstName.value} ${fullName.lastName.value}`
        );
        this.shortName = value(`${fullName.firstName.value[0].toUpperCase()} ${fullName.lastName.value[0].toUpperCase()}`);
        this.photoUrl = value(photo?.url ?? null);
    }

    public updatePhoto = (photo: PhotoPath) => {
        this.photoUrl = value(photo?.url ?? null);
    };

    public updateFullName = (fullName: FullName) => {
        this.fullName = value(
            `${fullName.firstName.value} ${fullName.lastName.value}`
        );
        this.shortName = value(fullName.firstName.value[0].toUpperCase());
    };
}

export default AvatarButtonViewModel;
