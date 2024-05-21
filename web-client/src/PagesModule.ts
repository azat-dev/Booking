import PageMainVMImpl from "./presentation/pages/page-main/PageMainVMImpl";
import ComponentsModule from "./presentation/app/app-model/ComponentsModule";
import PageAccommodationDetailsVM from "./presentation/pages/page-accommodation-details/PageAccommodationDetailsVM";
import Accommodation from "./domain/accommodations/Accommodation";
import PageUserProfileVM from "./presentation/pages/page-user-profile/PageUserProfileVM";
import AppSessionAuthenticated from "./domain/auth/entities/AppSessionAuthenticated";
import OpenFileDialogForUploadingUserPhoto from "./presentation/commands/OpenFileDialogForUploadingUserPhoto";
import Bus from "./domain/utils/Bus";

class PagesModule {

    public constructor(
        private readonly components: ComponentsModule,
        private readonly bus: Bus
    ) {
    }

    public mainPage = () => {
        return new PageMainVMImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = (accomodation: Accommodation) => {
        return new PageAccommodationDetailsVM(
            accomodation,
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        )
    }

    public profilePage = (session: AppSessionAuthenticated) => {
        return new PageUserProfileVM(
            session.userInfo,
            () => {
                this.bus.publish(new OpenFileDialogForUploadingUserPhoto());
            }
        )
    }
}

export default PagesModule;