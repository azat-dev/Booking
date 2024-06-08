import ComponentsConfig from "./ComponentsConfig";
import Accommodation from "../../../domain/accommodations/Accommodation";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";
import OpenFileDialogForUploadingUserPhoto from "../../commands/OpenFileDialogForUploadingUserPhoto";
import Bus, {matchClasses} from "../../../domain/utils/Bus";
import UpdatedUserPhoto from "../../../domain/auth/events/UpdatedUserPhoto.ts";
import FailedUpdateUserPhoto from "../../../domain/auth/events/FailedUpdateUserPhoto.ts";
import Listings from "../../../domain/listings/entities/Listings.ts";
import PageEditListingVM from "../../pages/page-edit-listing/PageEditListingVM.ts";

class PagesConfig {
    public constructor(
        private readonly components: ComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public mainPage = async () => {
        const imp = import("../../pages/page-main/PageMainVMImpl");
        const PageMainVMImpl = (await imp).default;

        return new PageMainVMImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = async (accomodation: Accommodation) => {
        const imp = import("../../pages/page-accommodation-details/PageAccommodationDetailsVM");
        const PageAccommodationDetailsVM = (await imp).default;
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
            this.components.navigationBar(),
            session.userInfo,
            async () => {
                const response = await this.bus.publishAndWaitFor(
                    new OpenFileDialogForUploadingUserPhoto(),
                    matchClasses(UpdatedUserPhoto, FailedUpdateUserPhoto)
                );

                switch (response.constructor) {
                    case UpdatedUserPhoto:
                        break;
                    case FailedUpdateUserPhoto:
                        throw new Error("Failed to update user photo");
                }
            }
        )
    }

    public listingsPage = async (session: AppSessionAuthenticated) => {

        const PageUserProfileVM = (await import("../../pages/page-listings/PageListingsVM.ts")).default;
        const navigationBar = await this.components.hostingNavigationBar();

        return new PageUserProfileVM(
            new Listings(),
            navigationBar
        )
    }

    public editListingPage = async (session: AppSessionAuthenticated) => {

        const PageEditListingVM = (await import("../../pages/page-edit-listing/PageEditListingVM")).default;

        return new PageEditListingVM(
        )
    }
}

export default PagesConfig;