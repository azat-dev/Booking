// import AppVM, {ActiveDialogType, ActiveDialogVM, } from "./AppVM";
// import Subject from "../../utils/binding/Subject";
// import value from "../../utils/binding/value";
// import CurrentSessionStore from "../../../domain/auth/CurrentSession/CurrentSessionStore";
// import LoginDialogVM from "../../dialogs/login-dialog/LoginDialogVM";
// import SessionStatus from "../../../domain/auth/CurrentSession/Session/SessionStatus";
// import SignUpDialogVM from "../../dialogs/sign-up-dialog/SignUpDialogVM";
// import PageMainVMImpl from "../../pages/page-main/PageMainVMImpl";
// import PageMainVM from "../../pages/page-main/PageMainVM";
// import PageAccommodationDetailsVM from "../../pages/page-accommodation-details/PageAccommodationDetailsVM";
// import AccommodationsRegistry from "../../../domain/accommodations/AccommodationsRegistry";
// import AccommodationId from "../../../domain/accommodations/AccommodationId";
// import ReservationService from "../../../domain/booking/ReservationService";
// import SignUpByEmailData from "../../../domain/auth/interfaces/services/SignUpByEmailData";
// import {AuthenticateByEmailData} from "../../../domain/auth/interfaces/services/AuthService";
// import {Session} from "../../../domain/auth/CurrentSession/Session/Session";
// import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
// import ProfileButtonAnonymousVM
//     from "../../components/navigation-bar/profile-button-anonymous/ProfileButtonAnonymousVM";
// import ProfileButtonAuthenticatedVM
//     from "../../components/navigation-bar/profile-button-authenticated/ProfileButtonAuthenticatedVM";
// import SessionAuthenticated from "../../../domain/auth/CurrentSession/Session/SessionAuthenticated";
// import ProfileButtonVM from "../../components/navigation-bar/profile-button/ProfileButtonVM";
// import PageUserProfileVM from "../../pages/page-user-profile/PageUserProfileVM";
// import NavigationDelegate from "./NavigationDelegate";
// import Page from "../Page";
//
// class AppVMImpl implements AppVM {
//     public activeDialog: Subject<ActiveDialogVM | null>;
//     public currentPage: Subject<Page | null> = value({type: "loading"});
//     public navigationDelegate: NavigationDelegate | null = null
//
//     public constructor(
//         private currentSession: CurrentSessionStore,
//         private accommodationsRegistry: AccommodationsRegistry,
//         private reservationService: ReservationService
//     ) {
//         this.activeDialog = value(null);
//         currentSession.current.listen(
//             this.closeAuthenticationDialogsIfAuthenticated
//         );
//     }
//
//     public openLoginDialog = async (): Promise<void> => {
//         this.activeDialog.set({
//             type: ActiveDialogType.Login,
//             vm: new LoginDialogVM(
//                 this.authenticateByEmail,
//                 this.closeDialog,
//                 this.openSignUpDialog
//             ),
//         });
//     };
//
//     public openSignUpDialog = (): void => {
//         this.activeDialog.set({
//             type: ActiveDialogType.SignUp,
//             vm: new SignUpDialogVM(
//                 this.signUpByEmail,
//                 this.closeDialog,
//                 this.openLoginDialog
//             ),
//         });
//     };
//
//     public toggleFavorite = (id: string): void => {
//         const currentUserSession = this.currentSession.current.value;
//         if (currentUserSession.type !== SessionStatus.AUTHENTICATED) {
//             this.openSignUpDialog();
//             return;
//         }
//
//         throw new Error("Not implemented");
//     };
//
//     public makeMainPage = async (): Promise<PageMainVM> => {
//
//         await new Promise(resolve => setTimeout(resolve, 1000));
//         return new PageMainVMImpl(
//             this.makeNavigationBar(),
//             this.toggleFavorite
//         );
//     }
//
//     public runMainPage = async (): Promise<void> => {
//         this.currentPage.set({
//             type: "loading"
//         });
//
//         this.currentPage.set({
//             type: "main",
//             vm: await this.makeMainPage()
//         });
//     }
//
//     public makeAccommodationDetailsPage = async (
//         accommodationId: AccommodationId
//     ): Promise<PageAccommodationDetailsVM> => {
//         await new Promise(resolve => setTimeout(resolve, 1000));
//         const accommodation =
//             await this.accommodationsRegistry.getAccommodationById(
//                 accommodationId
//             );
//         return new PageAccommodationDetailsVM(
//             accommodation,
//             this.makeNavigationBar(),
//             this.reservationService.getAccommodationCost
//         );
//     };
//
//     public runAccommodationDetailsPage = async (
//         accommodationId: AccommodationId
//     ): Promise<void> => {
//         this.currentPage.set({
//             type: "loading"
//         });
//
//         this.currentPage.set({
//             type: "accommodation-details",
//             vm: await this.makeAccommodationDetailsPage(accommodationId)
//         });
//     }
//
//     public makeProfilePage = async (session: SessionAuthenticated): Promise<PageUserProfileVM> => {
//         return new PageUserProfileVM(
//             session.getUserInfo(),
//             () => {
//                 throw new Error("Not implemented");
//             }
//         );
//     };
//
//     private runRedirectToMainPage = async (): Promise<void> => {
//
//         this.navigationDelegate?.navigateToMainPage(true);
//         this.closeDialog();
//         await this.openLoginDialog()
//     }
//
//     private openProfilePage = async (session: SessionAuthenticated): Promise<void> => {
//         this.currentPage.set({
//             type: "profile",
//             vm: await this.makeProfilePage(session)
//         });
//     }
//
//     public runProfilePage = async (): Promise<void> => {
//
//         if (this.currentPage.value?.type === "profile") {
//             return;
//         }
//
//         this.currentPage.set({
//             type: "loading"
//         });
//
//         const session = this.currentSession.current.value;
//
//         switch (session.type) {
//             case SessionStatus.AUTHENTICATED:
//                 await this.openProfilePage(session);
//                 break;
//             case SessionStatus.ANONYMOUS:
//                 await this.runRedirectToMainPage();
//                 break;
//
//             case SessionStatus.PROCESSING:
//                 break;
//         }
//     }
//
//     private closeAuthenticationDialogsIfAuthenticated = (
//         session: Session
//     ): void => {
//         const activeDialog = this.activeDialog.value;
//         if (
//             activeDialog &&
//             session.type === SessionStatus.AUTHENTICATED &&
//             [ActiveDialogType.Login, ActiveDialogType.SignUp].includes(
//                 activeDialog.type
//             )
//         ) {
//             this.closeDialog();
//         }
//     };
//
//     private closeDialog = (): void => {
//         this.activeDialog.set(null);
//     };
//
//     private authenticateByEmail = async (data: AuthenticateByEmailData) => {
//         const session = this.currentSession.current.value;
//
//         if (session.type !== SessionStatus.ANONYMOUS) {
//             return;
//         }
//
//         await session.authenticate(data);
//     };
//
//     private signUpByEmail = async (data: SignUpByEmailData): Promise<void> => {
//         const session = this.currentSession.current.value;
//
//         if (session.type !== SessionStatus.ANONYMOUS) {
//             return;
//         }
//
//         await session.signUpByEmail(data);
//     };
//
//     private logout = async () => {
//         const session = this.currentSession.current.value;
//
//         if (session.type !== SessionStatus.AUTHENTICATED) {
//             return;
//         }
//
//         await session.logout();
//     }
//
//     private makeNavigationBar = () => {
//         const profileButton = new ProfileButtonVM(
//             this.currentSession,
//             () =>
//                 new ProfileButtonAnonymousVM(
//                     this.openLoginDialog,
//                     this.openSignUpDialog
//                 ),
//             () => {
//                 const session = this.currentSession.current
//                     .value as SessionAuthenticated;
//                 const userInfo = session.getUserInfo();
//
//                 return new ProfileButtonAuthenticatedVM(
//                     userInfo.fullName,
//                     userInfo.email,
//                     userInfo.photo,
//                     () => {
//                         this.navigationDelegate?.navigateToProfilePage(false);
//                     },
//                     this.logout
//                 );
//             }
//         );
//
//         return new NavigationBarVM(
//             profileButton
//         );
//     };
// }
//
// export default AppVMImpl;

export default class AppVMImpl {

}