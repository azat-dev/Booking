import GuestComponentsConfig from "../guest/GuestComponentsConfig.ts";
import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import OpenFileDialogForUploadingUserPhoto from "../../../../commands/OpenFileDialogForUploadingUserPhoto.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import UpdatedUserPhoto from "../../../../../domain/auth/events/personal-info/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../../../../../domain/auth/events/personal-info/FailedUpdateUserPhoto.ts";
import mappedValue from "../../../../utils/binding/mappedValue.ts";
import UserPhotoVM from "../../../../pages/page-user-profile/user-photo/UserPhotoVM.ts";
import UploadingUserPhoto from "../../../../../domain/auth/events/personal-info/UploadingUserPhoto.ts";

class PagesConfig {
    public constructor(
        private readonly components: GuestComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public profilePage = async (session: AppSessionAuthenticated) => {

        const PageUserProfileVM = (await import("../../../../pages/page-user-profile/PageUserProfileVM.ts")).default;

        const userInfo = session.userInfo;

        const fullName = mappedValue(userInfo, (v) => v.fullName);
        const photo = mappedValue(userInfo, v => v.photo);

        const photoVM = new UserPhotoVM(fullName, photo);

        photoVM.listenOwnEvents(this.bus, event => {

            if (event instanceof UploadingUserPhoto) {
                photoVM.displayUploading();
            }

            if (event instanceof UpdatedUserPhoto) {
                photoVM.displayUploaded();
            }

            if (event instanceof FailedUpdateUserPhoto) {
                photoVM.displayFailedUpload();
            }
        });

        photoVM.delegate = {
            openUploadDialog: async () => {
                this.bus.publish(
                    new OpenFileDialogForUploadingUserPhoto()
                        .withSender(photoVM)
                );
            }
        };

        const vm = new PageUserProfileVM(
            this.components.navigationBar(),
            photoVM,
            userInfo,
        );

        return vm;
    }
}

export default PagesConfig;