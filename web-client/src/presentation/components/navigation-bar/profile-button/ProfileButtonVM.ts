import ProfileButtonAnonymousViewModel from "../profile-button-anonymous/ProfileButtonAnonymousViewModel";
import ProfileButtonAuthenticatedViewModel from "../profile-button-authenticated/ProfileButtonAuthenticatedViewModel";
import ProfileButtonLoadingVM from "../profile-button-loading/ProfileButtonLoadingVM";


type ProfileButtonVM =
    ProfileButtonAuthenticatedViewModel
    | ProfileButtonAnonymousViewModel
    | ProfileButtonLoadingVM;

export default ProfileButtonVM;

