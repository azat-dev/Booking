import AppViewModel, {
    ActiveDialogViewModel,
    ActiveDialogType,
} from "./AppViewModel";
import Subject from "../utils/binding/Subject";
import value from "../utils/binding/value";
import CurrentSessionStore from "../../domain/auth/CurrentSession/CurrentSessionStore";
import CurrentSessionStoreImpl from "../../domain/auth/CurrentSession/CurrentSessionStoreImpl";
import LocalStorageTokensRepository from "../../LocalStorageTokensRepository";
import AuthServiceImpl from "../../data/auth/services/AuthServiceImpl";
import LoginDialogViewModel from "../dialogs/login-dialog/LoginDialogViewModel";
import SessionStatus from "../../domain/auth/CurrentSession/Session/SessionStatus";
import SignUpDialogViewModel from "../dialogs/sign-up-dialog/SignUpDialogViewModel";
import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";
import PageMainViewModel from "../pages/page-main/PageMainViewModel";
import PageAccommodationDetailsViewModel from "../pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import PageAccommodationDetails from "../pages/page-accommodation-details/PageAccommodationDetails";
import Accommodation, {
    AccommodationType,
    Owner,
    RoomInfo,
    Title,
    Location,
    Description,
    Photo,
} from "../../domain/accommodations/Accommodation";
import Avatar from "../../domain/auth/values/Avatar";

class AppViewModelImpl implements AppViewModel {
    public activeDialog: Subject<ActiveDialogViewModel | null>;
    private currentSession: CurrentSessionStore;

    public constructor() {
        this.activeDialog = value(null);

        const tokensRepository = new LocalStorageTokensRepository();
        const authService = new AuthServiceImpl();

        this.currentSession = new CurrentSessionStoreImpl(
            tokensRepository,
            authService
        );
    }

    private closeDialog = (): void => {
        this.activeDialog.set(null);
    };

    public openLoginDialog = (): void => {
        this.activeDialog.set({
            type: ActiveDialogType.Login,
            vm: new LoginDialogViewModel(
                async (email, password) => {
                    const session = this.currentSession.current.value;

                    if (session.type !== SessionStatus.ANONYMOUS) {
                        return;
                    }

                    await session.authenticate(email, password);
                },
                this.closeDialog,
                this.openSignUpDialog
            ),
        });
    };

    public openSignUpDialog = (): void => {
        this.activeDialog.set({
            type: ActiveDialogType.SignUp,
            vm: new SignUpDialogViewModel(
                async (email, password) => {
                    const session = this.currentSession.current.value;

                    if (session.type !== SessionStatus.ANONYMOUS) {
                        return;
                    }

                    await session.authenticate(email, password);
                },
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

    public makeMainPage = (): PageMainViewModel => {
        return new PageMainViewModelImpl(
            this.openLoginDialog,
            this.openSignUpDialog,
            this.toggleFavorite
        );
    };

    public makeAccommodationDetailsPage =
        (): PageAccommodationDetailsViewModel => {
            const accommodation = new Accommodation(
                new Title("Small House"),
                AccommodationType.House,
                new Location("USA", "New York"),
                new Description(
                    "This is a small house in the middle of the city. It has a small garden and a garage. It is perfect for a small family. It is close to the city center. It has a small kitchen and a living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. "
                ),
                new Owner(
                    "John Doe",
                    new Avatar("https://example.com/avatar.jpg")
                ),
                [
                    new Photo("https://picsum.photos/id/11/200/300"),
                    new Photo("https://picsum.photos/id/12/200/300"),
                    new Photo("https://picsum.photos/id/255/200/300"),
                ],
                2,
                [
                    new RoomInfo(1, "Bedroom"),
                    new RoomInfo(1, "Living room"),
                    new RoomInfo(1, "Kitchen"),
                    new RoomInfo(1, "Bathroom"),
                ],
                4.5
            );
            return new PageAccommodationDetailsViewModel(
                accommodation,
                this.openLoginDialog,
                this.openSignUpDialog
            );
        };
}

export default AppViewModelImpl;
