import FullName from "../../../../domain/auth/values/FullName.ts";
import AvatarButtonVM from "./avatar-button/AvatarButtonVM.ts";
import Subject, {ReadonlySubject} from "../../../utils/binding/Subject.ts";
import Email from "../../../../domain/auth/values/Email.ts";
import PhotoPath from "../../../../domain/auth/values/PhotoPath.ts";
import value from "../../../utils/binding/value.ts";
import Disposables from "../../../utils/binding/Disposables.ts";
import KeepType from "../../../../domain/utils/KeepType.ts";

class ProfileButtonAuthenticatedVM extends KeepType {

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

        super();
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
