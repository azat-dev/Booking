import ProfileButtonVM from "../../../../components/profile-button/ProfileButtonVM.ts";
import Bus from "../../../../../domain/utils/Bus.ts";
import ProfileButtonAnonymousVM
    from "../../../../components/profile-button/profile-button-anonymous/ProfileButtonAnonymousVM.ts";
import ProfileButtonAuthenticatedVM
    from "../../../../components/profile-button/profile-button-authenticated/ProfileButtonAuthenticatedVM.ts";
import AppSession, {SessionState} from "../../../../../domain/auth/entities/AppSession.ts";
import AppSessionAuthenticated from "../../../../../domain/auth/entities/AppSessionAuthenticated.ts";
import value from "../../../../utils/binding/value.ts";
import AppSessionAnonymous from "../../../../../domain/auth/entities/AppSessionAnonymous.ts";
import AppSessionLoading from "../../../../../domain/auth/entities/AppSessionLoading.ts";
import NavigationBarVM from "../../../../components/navigation-bar/NavigationBarVM.ts";
import Subject from "../../../../utils/binding/Subject.ts";
import ProfileButtonLoadingVM
    from "../../../../components/profile-button/profile-button-loading/ProfileButtonLoadingVM.ts";
import NavigationDelegate from "../../../NavigationDelegate.ts";
import mappedValue from "../../../../utils/binding/mappedValue.ts";
import IdentityCommands from "../../domain/IdentityCommands.ts";
import CommonDialogsCommands from "../common/CommonDialogsCommands.ts";

class GuestComponentsConfig {

    public navigation: NavigationDelegate | null = null;

    public constructor(
        private readonly identityCommands: IdentityCommands,
        private readonly commonDialogsCommands: CommonDialogsCommands,
        private readonly appSession: AppSession,
        private readonly bus: Bus,
    ) {
    }

    public profileButton = (): Subject<ProfileButtonVM> => {

        const sessionSubject = this.appSession.state;
        const toButton = (session: SessionState): ProfileButtonVM => {

            switch (session.type) {
                case AppSessionAuthenticated.type:
                    return this.authenticatedProfileButton(session as AppSessionAuthenticated);

                case AppSessionAnonymous.type:
                    return this.anonymousProfileButton();

                case AppSessionLoading.type:
                    return this.loadingProfileButton();

                default:
                    throw new Error(`Unknown session state: ${session}`);
            }
        }

        const button = value(toButton(sessionSubject.value));
        sessionSubject.listen((session) => {
            button.set(toButton(session));
        });

        return button;
    };

    public navigationBar = () => {
        const showHostingButton = mappedValue(
            this.appSession.state,
            (session) => session.type === AppSessionAuthenticated.type
        );
        return new NavigationBarVM(
            this.profileButton(),
            showHostingButton
        )
    }

    private loadingProfileButton = (): ProfileButtonLoadingVM => {
        return new ProfileButtonLoadingVM();
    }

    private anonymousProfileButton = (): ProfileButtonAnonymousVM => {

        return new ProfileButtonAnonymousVM(
            this.commonDialogsCommands.openLoginDialog,
            this.commonDialogsCommands.openSignUpDialog,
        );
    }

    private authenticatedProfileButton = (session: AppSessionAuthenticated): ProfileButtonAuthenticatedVM => {

        const userInfo = session.userInfo;
        const fullName = mappedValue(userInfo, u => u.fullName);
        const email = mappedValue(userInfo, u => u.email);
        const photo = mappedValue(userInfo, u => u.photo);

        const button = new ProfileButtonAuthenticatedVM(
            fullName,
            email,
            photo,
            () => {
                this.navigation?.openUserProfilePage();
            },
            () => {
                this.navigation?.openUserProfilePage();
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

export default GuestComponentsConfig;