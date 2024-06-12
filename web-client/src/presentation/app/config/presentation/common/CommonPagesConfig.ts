import GuestComponentsConfig from "../guest/GuestComponentsConfig.ts";
import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import mappedValue from "../../../../utils/binding/mappedValue.ts";
import UserPhotoVM from "../../../../pages/page-user-profile/user-photo/UserPhotoVM.ts";
import ProfileCommands from "../ProfileCommands.ts";
import UploadingUserPhoto from "../../../../../domain/auth/events/personal-info/UploadingUserPhoto.ts";
import UpdatedUserPhoto from "../../../../../domain/auth/events/personal-info/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../../../../../domain/auth/events/personal-info/FailedUpdateUserPhoto.ts";
import IdentityCommands from "../../domain/IdentityCommands.ts";

class PagesConfig {
    public constructor(
        private readonly identityCommands: IdentityCommands,
        private readonly components: GuestComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public profilePage = async (session: AppSessionAuthenticated) => {

        const PageUserProfileVM = (await import("../../../../pages/page-user-profile/PageUserProfileVM.ts")).default;

        const userInfo = session.userInfo;

        const fullName = mappedValue(userInfo, (v) => v.fullName);
        const photo = mappedValue(userInfo, v => v.photo);

        const profileCommands = new ProfileCommands(
            this.identityCommands,
            session,
            this.bus.publish
        );

        const photoVM = new UserPhotoVM(
            fullName,
            photo,
            profileCommands.openFileDialogForUploadingUserPhoto
        );

        photoVM.cleanOnDestroy(
            this.bus.subscribe(event => {
                if (event instanceof UploadingUserPhoto) {
                    photoVM.displayUploading();
                }

                if (event instanceof UpdatedUserPhoto) {
                    photoVM.displayUploaded();
                }

                if (event instanceof FailedUpdateUserPhoto) {
                    photoVM.displayFailedUpload();
                }
            })
        )

        return new PageUserProfileVM(
            this.components.navigationBar(),
            photoVM,
            userInfo,
        );
    }
}

export default PagesConfig;