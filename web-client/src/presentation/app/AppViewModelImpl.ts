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
import DatesRange from "../../domain/booking/values/DatesRange";
import GuestsQuantity from "../../domain/booking/values/GuestsQuantity";
import Cost from "../../domain/booking/values/Cost";
import ReservationService from "../../domain/booking/ReservationService";

class AppViewModelImpl implements AppViewModel {
    public activeDialog: Subject<ActiveDialogViewModel | null>;

    public constructor(
        private currentSession: CurrentSessionStore,
        private accommodationsRegistry: AccommodationsRegistry,
        private reservationService: ReservationService
    ) {
        this.activeDialog = value(null);
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

    public makeMainPage = async (): Promise<PageMainViewModel> => {
        return new PageMainViewModelImpl(
            this.openLoginDialog,
            this.openSignUpDialog,
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
            this.openLoginDialog,
            this.openSignUpDialog,
            this.reservationService.getAccommodationCost
        );
    };
}

export default AppViewModelImpl;
