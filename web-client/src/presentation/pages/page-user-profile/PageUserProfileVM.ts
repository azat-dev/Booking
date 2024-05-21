import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import UserPhotoVM from "./user-photo/UserPhotoVM";
import {ReadonlySubject} from "../../utils/binding/Subject";
import Disposables from "../../utils/binding/Disposables";
import value from "../../utils/binding/value";
import Page from "../../app/Page";

class PageUserProfileVM extends Page {

    public static readonly TYPE = "PAGE_USER_PROFILE_VM";
    public readonly photo: UserPhotoVM;
    private disposables = new Disposables();

    public constructor(
        private userInfo: ReadonlySubject<PersonalUserInfo>,
        private onOpenUploadPhotoDialog: () => void
    ) {

        super();

        const fullName = value(userInfo.value.fullName);
        const photo = value(userInfo.value.photo);

        this.disposables.addItems(
            userInfo.listen((newUser) => {
                fullName.set(newUser.fullName);
                photo.set(newUser.photo)
            }),
        );

        this.photo = new UserPhotoVM(
            fullName,
            photo,
            async () => {
            }
        );
    }

    public get type() {
        return PageUserProfileVM.TYPE;
    }

    public updatePhoto = () => {
        this.onOpenUploadPhotoDialog();
    }

    public updateUser = (user: PersonalUserInfo) => {

    }

    public dispose = () => {
        this.disposables.dispose();
    }
}

export default PageUserProfileVM;