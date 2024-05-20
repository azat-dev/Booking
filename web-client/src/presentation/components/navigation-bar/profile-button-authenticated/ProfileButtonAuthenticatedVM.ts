import FullName from "../../../../domain/auth/values/FullName";
import AvatarButtonVM from "./avatar-button/AvatarButtonVM";
import {ReadonlySubject} from "../../../utils/binding/Subject";
import Email from "../../../../domain/auth/values/Email";
import {PhotoPath} from "../../../../domain/auth/values/PhotoPath";

class ProfileButtonAuthenticatedVM {

    public static readonly TYPE = "ProfileButtonAuthenticatedVM";

    public get type() {
        return (this.constructor as any).TYPE;
    }

    public avatar: AvatarButtonVM;

    public constructor(
        readonly fullName: ReadonlySubject<FullName>,
        readonly email: ReadonlySubject<Email>,
        readonly photo: ReadonlySubject<PhotoPath | null>,
        private readonly onOpenProfile: () => void,
        private readonly onLogout: () => void
    ) {
        this.avatar = new AvatarButtonVM(fullName, photo);
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

    public dispose = () => {
        this.avatar.dispose();
    }
}

export default ProfileButtonAuthenticatedVM;
