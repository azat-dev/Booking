import FullName from "../../../../domain/auth/CurrentSession/Session/FullName";
import AvatarButtonViewModel from "./avatar-button/AvatarButtonViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import Email from "../../../../domain/auth/values/Email";
import {PhotoPath} from "../../../../domain/auth/values/PhotoPath";

class ProfileButtonAuthenticatedViewModel {
    public avatar: AvatarButtonViewModel;

    public fullName: Subject<string>;
    public email: Subject<string>;

    public constructor(
        fullName: FullName,
        email: Email,
        photo: PhotoPath | null,
        private readonly onOpenProfile: () => void,
        private readonly onLogout: () => void
    ) {
        this.avatar = new AvatarButtonViewModel(fullName, photo);
        this.fullName = value(fullName.toString());
        this.email = value(email.toString());
    }

    public openProfile = () => {
        this.onOpenProfile();
    };

    public openHelp = () => {
        console.log("Help");
    }

    public logout = () => {
        this.onLogout();
    };

    public updateFullName = (fullName: FullName) => {
        this.avatar.updateFullName(fullName);
    };

    public updatePhoto = (photo: PhotoPath) => {
        this.avatar.updatePhoto(photo);
    };
}

export default ProfileButtonAuthenticatedViewModel;
