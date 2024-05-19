import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";
import PageAccommodationDetailsViewModel from "../pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import PageUserProfileVM from "../pages/page-user-profile/PageUserProfileVM";

type Page = {
    type: "main",
    vm: PageMainViewModelImpl
} | {
    type: "accommodation-details",
    vm: PageAccommodationDetailsViewModel
} | {
    type: "profile",
    vm: PageUserProfileVM
} | {
    type: "loading",
};

export default Page;