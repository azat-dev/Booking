import AppViewModel, {
    ActiveDialogViewModel,
    ActiveDialogType,
} from "./AppViewModel";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import CurrentSessionStore from "../../domain/auth/CurrentSession/CurrentSessionStore";
import LoginDialogViewModel from "../dialogs/login-dialog/LoginDialogViewModel";
import SessionStatus from "../../domain/auth/CurrentSession/Session/SessionStatus";
import SignUpDialogViewModel from "../dialogs/sign-up-dialog/SignUpDialogViewModel";
import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";
import PageMainViewModel from "../pages/page-main/PageMainViewModel";
import PageAccommodationDetailsViewModel from "../pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import AccommodationsRegistry from "../../domain/accommodations/AccommodationsRegistry";
import AccommodationId from "../../domain/accommodations/AccommodationId";
import ReservationService from "../../domain/booking/ReservationService";
import SignUpByEmailData from "../../domain/auth/CurrentSession/Session/SignUpByEmailData";
import { AuthenticateByEmailData } from "../../domain/auth/CurrentSession/Session/AuthService";
import { Session } from "../../domain/auth/CurrentSession/Session/Session";
import NavigationBarViewModel from "../components/navigation-bar/NavigationBarViewModel";
import ProfileButtonAnonymousViewModel from "../components/navigation-bar/profile-button-anonymous/ProfileButtonAnonymousViewModel";
import ProfileButtonAuthenticatedViewModel from "../components/navigation-bar/profile-button-authenticated/ProfileButtonAuthenticatedViewModel";
import SessionAuthenticated from "../../domain/auth/CurrentSession/Session/SessionAuthenticated";
import ProfileButtonViewModel from "../components/navigation-bar/profile-button/ProfileButtonViewModel";

class AppViewModelImpl implements AppViewModel {
    public activeDialog: Subject<ActiveDialogViewModel | null>;

    public constructor(
        private currentSession: CurrentSessionStore,
        private accommodationsRegistry: AccommodationsRegistry,
        private reservationService: ReservationService
    ) {
        this.activeDialog = value(null);
        currentSession.current.listen(
            this.closeAuthenticationDialogsIfAuthenticated
        );
    }

    private closeAuthenticationDialogsIfAuthenticated = (
        session: Session
    ): void => {
        const activeDialog = this.activeDialog.value;
        if (
            activeDialog &&
            session.type === SessionStatus.AUTHENTICATED &&
            [ActiveDialogType.Login, ActiveDialogType.SignUp].includes(
                activeDialog.type
            )
        ) {
            this.closeDialog();
        }
    };

    private closeDialog = (): void => {
        this.activeDialog.set(null);
    };

    private authenticateByEmail = async (data: AuthenticateByEmailData) => {
        const session = this.currentSession.current.value;

        if (session.type !== SessionStatus.ANONYMOUS) {
            return;
        }

        await session.authenticate(data);
    };

    public openLoginDialog = (): void => {
        this.activeDialog.set({
            type: ActiveDialogType.Login,
            vm: new LoginDialogViewModel(
                this.authenticateByEmail,
                this.closeDialog,
                this.openSignUpDialog
            ),
        });
    };

    private signUpByEmail = async (data: SignUpByEmailData): Promise<void> => {
        const session = this.currentSession.current.value;

        if (session.type !== SessionStatus.ANONYMOUS) {
            return;
        }

        await session.signUpByEmail(data);
    };

    public openSignUpDialog = (): void => {
        this.activeDialog.set({
            type: ActiveDialogType.SignUp,
            vm: new SignUpDialogViewModel(
                this.signUpByEmail,
                this.closeDialog,
                this.openLoginDialog
            ),
        });
    };

    public toggleFavorite = (id: string): void => {
        const currentUserSession = this.currentSession.current.value;
        if (currentUserSession.type !== SessionStatus.AUTHENTICATED) {
            this.openSignUpDialog();
            return;
        }

        throw new Error("Not implemented");
    };

    private logout = async () => {
        const session = this.currentSession.current.value;

        if (session.type !== SessionStatus.AUTHENTICATED) {
            return;
        }

        session.logout();
    }

    private makeNavigationBar = () => {
        const profileButton = new ProfileButtonViewModel(
            this.currentSession,
            () =>
                new ProfileButtonAnonymousViewModel(
                    this.openLoginDialog,
                    this.openSignUpDialog
                ),
            () => {
                const session = this.currentSession.current
                    .value as SessionAuthenticated;
                const userInfo = session.getUserInfo();

                return new ProfileButtonAuthenticatedViewModel(
                    userInfo.fullName,
                    userInfo.email,
                    userInfo.photoUrl,
                    () => {
                        throw new Error("Not implemented");
                    },
                    this.logout
                );
            }
        );

        return new NavigationBarViewModel(
            profileButton
        );
    };

    public makeMainPage = async (): Promise<PageMainViewModel> => {
        return new PageMainViewModelImpl(
            this.makeNavigationBar(),
            this.toggleFavorite
        );
    };

    public makeAccommodationDetailsPage = async (
        accommodationId: AccommodationId
    ): Promise<PageAccommodationDetailsViewModel> => {
        const accommodation =
            await this.accommodationsRegistry.getAccommodationById(
                accommodationId
            );
        return new PageAccommodationDetailsViewModel(
            accommodation,
            this.makeNavigationBar(),
            this.reservationService.getAccommodationCost
        );
    };
}

export default AppViewModelImpl;
