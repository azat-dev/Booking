import ProfileButtonAuthenticatedVM from "../profile-button-authenticated/ProfileButtonAuthenticatedVM";
import ProfileButtonLoadingVM from "../profile-button-loading/ProfileButtonLoadingVM";
import ProfileButtonAnonymousVM from "../profile-button-anonymous/ProfileButtonAnonymousVM";


type ProfileButtonVM =
    ProfileButtonAuthenticatedVM
    | ProfileButtonAnonymousVM
    | ProfileButtonLoadingVM;

export default ProfileButtonVM;

