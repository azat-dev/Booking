import ProfileButtonVM from "../../components/navigation-bar/profile-button/ProfileButtonVM";
import Bus from "../../../domain/utils/Bus";
import ProfileButtonAnonymousVM
    from "../../components/navigation-bar/profile-button-anonymous/ProfileButtonAnonymousVM";
import ProfileButtonAuthenticatedVM
    from "../../components/navigation-bar/profile-button-authenticated/ProfileButtonAuthenticatedVM";
import AppSession, {SessionState} from "../../../domain/auth/entities/AppSession";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";
import value from "../../utils/binding/value";
import AppSessionAnonymous from "../../../domain/auth/entities/AppSessionAnonymous";
import AppSessionLoading from "../../../domain/auth/entities/AppSessionLoading";
import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
import Logout from "../../../domain/auth/commands/Logout";
import OpenLoginDialog from "../../commands/OpenLoginDialog";
import OpenSignUpDialog from "../../commands/OpenSignUpDialog";
import Subject from "../../utils/binding/Subject";
import ProfileButtonLoadingVM from "../../components/navigation-bar/profile-button-loading/ProfileButtonLoadingVM";

class ComponentsModule {

    public constructor(
        private readonly appSession: AppSession,
        private readonly bus: Bus,
    ) {
    }

    public profileButton = (): Subject<ProfileButtonVM> => {

        const sessionSubject = this.appSession.state;
        const toButton = (session: SessionState): ProfileButtonVM => {

            debugger
            switch (session.type) {
                case AppSessionAuthenticated.TYPE:
                    return this.authenticatedProfileButton(session as AppSessionAuthenticated);

                case AppSessionAnonymous.TYPE:
                    return this.anonymousProfileButton();

                case AppSessionLoading.TYPE:
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
        return new NavigationBarVM(
            this.profileButton()
        )
    }

    private loadingProfileButton = () : ProfileButtonLoadingVM  => {
        return new ProfileButtonLoadingVM();
    }

    private anonymousProfileButton = (): ProfileButtonAnonymousVM => {

        return new ProfileButtonAnonymousVM(
            () => {
                this.bus.publish(new OpenLoginDialog());
            },
            () => {
                this.bus.publish(new OpenSignUpDialog());
            }
        );
    }

    private authenticatedProfileButton = (session: AppSessionAuthenticated): ProfileButtonAuthenticatedVM => {

        const userInfo = session.userInfo;
        const fullName = value(userInfo.value.fullName);
        const email = value(userInfo.value.email);
        const photo = value(userInfo.value.photo);

        const disposables = [
            fullName,
            photo,
            email
        ];

        const button = new ProfileButtonAuthenticatedVM(
            fullName,
            email,
            photo,
            () => {
                this.bus.publish("open-profile-page");
            },
            () => {
                this.bus.publish(new Logout());
            }
        );

        const defaultDispose = button.dispose;

        button.dispose = () => {
            disposables.forEach((d) => d.dispose());
            defaultDispose();
        };

        return button;
    }
}

export default ComponentsModule;