import PageMainVMImpl from "../pages/page-main/PageMainVMImpl";
import PageAccommodationDetailsVM from "../pages/page-accommodation-details/PageAccommodationDetailsVM";
import PageUserProfileVM from "../pages/page-user-profile/PageUserProfileVM";

type Page = {
    type: "main",
    vm: PageMainVMImpl
} | {
    type: "accommodation-details",
    vm: PageAccommodationDetailsVM
} | {
    type: "profile",
    vm: PageUserProfileVM
} | {
    type: "loading",
};

export default Page;