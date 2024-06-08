import ProfileButtonAuthenticatedVM from "./profile-button-authenticated/ProfileButtonAuthenticatedVM.ts";
import ProfileButtonLoadingVM from "./profile-button-loading/ProfileButtonLoadingVM.ts";
import ProfileButtonAnonymousVM from "./profile-button-anonymous/ProfileButtonAnonymousVM.ts";


type ProfileButtonVM =
    ProfileButtonAuthenticatedVM
    | ProfileButtonAnonymousVM
    | ProfileButtonLoadingVM;

export default ProfileButtonVM;

