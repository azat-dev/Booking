import FullName from "../../../../../domain/auth/CurrentSession/Session/FullName";
import Avatar from "../../../../../domain/auth/values/Avatar";
import Subject from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";

class AvatarButtonViewModel {
    public fullName: Subject<string>;
    public shortName: Subject<string>;
    public photoUrl: Subject<string | null>;

    public constructor(fullName: FullName, avatar: Avatar | null) {
        this.fullName = value(
            `${fullName.firstName.value} ${fullName.lastName.value}`
        );
        this.shortName = value(`${fullName.firstName.value[0].toUpperCase()} ${fullName.lastName.value[0].toUpperCase()}`);
        this.photoUrl = value(avatar?.value ?? null);
    }

    public updateAvatar = (avatar: Avatar) => {
        this.photoUrl = value(avatar.value ?? null);
    };

    public updateFullName = (fullName: FullName) => {
        this.fullName = value(
            `${fullName.firstName.value} ${fullName.lastName.value}`
        );
        this.shortName = value(fullName.firstName.value[0].toUpperCase());
    };
}

export default AvatarButtonViewModel;
