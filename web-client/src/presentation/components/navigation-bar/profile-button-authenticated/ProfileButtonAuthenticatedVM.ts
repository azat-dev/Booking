import FullName from "../../../../domain/auth/values/FullName";
import AvatarButtonVM from "./avatar-button/AvatarButtonVM";
import Subject, {ReadonlySubject} from "../../../utils/binding/Subject";
import Email from "../../../../domain/auth/values/Email";
import {PhotoPath} from "../../../../domain/auth/values/PhotoPath";
import value from "../../../utils/binding/value";
import Disposables from "../../../utils/binding/Disposables";

class ProfileButtonAuthenticatedVM {

    public static readonly TYPE = "ProfileButtonAuthenticatedVM";

    public get type() {
        return (this.constructor as any).TYPE;
    }

    public readonly fullName: Subject<string>;
    public readonly email: Subject<string>;

    public avatar: AvatarButtonVM;

    private readonly disposables = new Disposables();

    public constructor(
        fullName: ReadonlySubject<FullName>,
        email: ReadonlySubject<Email>,
        readonly photo: ReadonlySubject<PhotoPath | null>,
        private readonly onOpenProfile: () => void,
        private readonly onLogout: () => void
    ) {
        this.avatar = new AvatarButtonVM(fullName, photo);
        this.email = value(email.value.toString());
        this.fullName = value(fullName.value.toString());

        this.disposables.add(
            email.listen((email) => {
                this.email.set(email.toString());
            })
        );

        this.disposables.add(
            fullName.listen((fullName) => {
                this.fullName.set(fullName.toString());
            })
        );
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
