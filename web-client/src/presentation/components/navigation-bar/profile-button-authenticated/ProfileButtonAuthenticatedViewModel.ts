import FullName from "../../../../domain/auth/CurrentSession/Session/FullName";
import Avatar from "../../../../domain/auth/values/Avatar";
import AvatarButtonViewModel from "./avatar-button/AvatarButtonViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import Email from "../../../../domain/auth/values/Email";

class ProfileButtonAuthenticatedViewModel {
    public avatar: AvatarButtonViewModel;

    public fullName: Subject<string>;
    public email: Subject<string>;

    public constructor(
        fullName: FullName,
        email: Email,
        avatar: Avatar | null,
        private readonly onOpenProfile: () => void,
        private readonly onLogout: () => void
    ) {
        this.avatar = new AvatarButtonViewModel(fullName, avatar);
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

    public updateAvatar = (avatar: Avatar) => {
        this.avatar.updateAvatar(avatar);
    };
}

export default ProfileButtonAuthenticatedViewModel;
