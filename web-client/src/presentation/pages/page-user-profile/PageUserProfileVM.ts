import PersonalUserInfo from "../../../domain/auth/CurrentSession/Session/entities/PersonalUserInfo";
import UserPhotoVM from "./user-photo/UserPhotoVM";

class PageUserProfileVM {

    public readonly photo: UserPhotoVM;

    public constructor(
        private user: PersonalUserInfo,
        private onOpenUploadPhotoDialog: () => void
    ) {

        this.photo = new UserPhotoVM(
            user.fullName,
            user.photo,
            async () => {
            }
        );
    }

    public updatePhoto = () => {
        this.onOpenUploadPhotoDialog();
    }

    public updateUser = (user: PersonalUserInfo) => {

    }
}

export default PageUserProfileVM;