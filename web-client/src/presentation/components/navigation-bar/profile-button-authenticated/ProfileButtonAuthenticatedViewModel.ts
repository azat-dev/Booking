import FullName from "../../../../domain/auth/CurrentSession/Session/FullName";
import Avatar from "../../../../domain/auth/values/Avatar";
import AvatarButtonViewModel from "./avatar-button/AvatarButtonViewModel";

class ProfileButtonAuthenticatedViewModel {
    public avatar: AvatarButtonViewModel;

    public constructor(
        fullName: FullName,
        avatar: Avatar | null,
        private readonly onOpenProfile: () => void,
        private readonly onLogout: () => void
    ) {
        this.avatar = new AvatarButtonViewModel(fullName, avatar);
    }

    public openProfile = () => {
        this.onOpenProfile();
    };

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
