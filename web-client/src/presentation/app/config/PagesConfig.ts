import PageMainVMImpl from "../../pages/page-main/PageMainVMImpl";
import ComponentsConfig from "./ComponentsConfig";
import PageAccommodationDetailsVM from "../../pages/page-accommodation-details/PageAccommodationDetailsVM";
import Accommodation from "../../../domain/accommodations/Accommodation";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";
import OpenFileDialogForUploadingUserPhoto from "../../commands/OpenFileDialogForUploadingUserPhoto";
import Bus from "../../../domain/utils/Bus";

class PagesConfig {
    public constructor(
        private readonly components: ComponentsConfig,
        private readonly bus: Bus
    ) {
    }
    public mainPage = async () => {
        return new PageMainVMImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = async (accomodation: Accommodation) => {
        return new PageAccommodationDetailsVM(
            accomodation,
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        )
    }

    public profilePage = async (session: AppSessionAuthenticated) => {

        const PageUserProfileVM = (await import("../../pages/page-user-profile/PageUserProfileVM")).default;

        return new PageUserProfileVM(
            session.userInfo,
            () => {
                this.bus.publish(new OpenFileDialogForUploadingUserPhoto());
            }
        )
    }
}

export default PagesConfig;