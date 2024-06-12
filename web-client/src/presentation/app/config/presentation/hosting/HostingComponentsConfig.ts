import ProfileButtonVM from "../../../../components/profile-button/ProfileButtonVM.ts";
import ProfileButtonAnonymousVM
    from "../../../../components/profile-button/profile-button-anonymous/ProfileButtonAnonymousVM.ts";
import ProfileButtonAuthenticatedVM
    from "../../../../components/profile-button/profile-button-authenticated/ProfileButtonAuthenticatedVM.ts";
import AppSession, {SessionState} from "../../../../../domain/auth/entities/AppSession.ts";
import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import value from "../../../../utils/binding/value.ts";
import AppSessionAnonymous from "../../../../../domain/auth/entities/AppSessionAnonymous.ts";
import AppSessionLoading from "../../../../../domain/auth/entities/AppSessionLoading.ts";
import Subject from "../../../../utils/binding/Subject.ts";
import ProfileButtonLoadingVM
    from "../../../../components/profile-button/profile-button-loading/ProfileButtonLoadingVM.ts";
import NavigationDelegate from "../../../NavigationDelegate.ts";
import HostingNavigationBarVM from "../../../../components/hosting-navigation-bar/HostingNavigationBarVM.ts";
import IdentityCommands from "../../domain/IdentityCommands.ts";
import CommonDialogsCommands from "../common/CommonDialogsCommands.ts";

class HostingComponentsConfig {

    public navigation: NavigationDelegate | null = null;

    public constructor(
        private readonly commonDialogsCommands: CommonDialogsCommands,
        private readonly identityCommands: IdentityCommands,
        private readonly appSession: AppSessionAuthenticated,
    ) {
    }

    public hostingNavigationBar = async () => {

        return new HostingNavigationBarVM(
            this.profileButton()
        )
    }


    private profileButton = (): ProfileButtonAuthenticatedVM => {

        const userInfo = this.appSession.userInfo;
        const fullName = value(userInfo.value.fullName);
        const email = value(userInfo.value.email);
        const photo = value(userInfo.value.photo);

        const button = new ProfileButtonAuthenticatedVM(
            fullName,
            email,
            photo,
            () => {
                this.navigation?.openUserProfilePage();
            },
            () => {
                alert("Help")
            },
            this.identityCommands.logout
        );

        button.cleanOnDestroy(
            fullName,
            photo,
            email
        );

        return button;
    }
}

export default HostingComponentsConfig;