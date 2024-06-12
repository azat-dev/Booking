import FullName from "../../../../domain/auth/values/FullName.ts";
import AvatarButtonVM from "./avatar-button/AvatarButtonVM.ts";
import Subject, {ReadonlySubject} from "../../../utils/binding/Subject.ts";
import Email from "../../../../domain/auth/values/Email.ts";
import PhotoPath from "../../../../domain/auth/values/PhotoPath.ts";
import value from "../../../utils/binding/value.ts";
import VM from "../../../utils/VM.ts";

class ProfileButtonAuthenticatedVM extends VM {

    public readonly fullName: Subject<string>;
    public readonly email: Subject<string>;

    public avatar: AvatarButtonVM;

    public constructor(
        fullName: ReadonlySubject<FullName>,
        email: ReadonlySubject<Email>,
        readonly photo: ReadonlySubject<PhotoPath | null>,
        private readonly _openProfile: () => void,
        private readonly _openHelp: () => void,
        private readonly _logout: () => void
    ) {

        super();
        this.avatar = new AvatarButtonVM(fullName, photo);
        this.email = value(email.value.toString());
        this.fullName = value(fullName.value.toString());

        this.cleanOnDestroy(
            email.listen((email) => {
                this.email.set(email.toString());
            })
        );

        this.cleanOnDestroy(
            fullName.listen((fullName) => {
                this.fullName.set(fullName.toString());
            })
        );
    }

    public openProfile = () => {
        this._openProfile();
    };

    public openHelp = () => {
        this._openHelp();
    }

    public logout = () => {
        this._logout();
    };
}

export default ProfileButtonAuthenticatedVM;
