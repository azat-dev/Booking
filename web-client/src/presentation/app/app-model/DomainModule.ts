import AppSessionImpl from "../../../domain/auth/entities/AppSessionImpl";
import AppSession from "../../../domain/auth/entities/AppSession";
import AppStarted from "../../../domain/auth/events/AppStarted";
import PersonalInfoDidChange from "../../../domain/auth/events/PersonalInfoDidChange";
import WhenPersonalInfoChangedUpdateAppSession
    from "../../../domain/auth/policies/WhenPersonalInfoChangedUpdateAppSession";
import UserLoggedOut from "../../../domain/auth/events/UserLoggedOut";
import WhenLogoutThenUpdateAppSession from "../../../domain/auth/policies/WhenLogoutThenUpdateAppSession";
import UserLoggedIn from "../../../domain/auth/events/UserLoggedIn";
import WhenLoggedInThenUpdateAppSession from "../../../domain/auth/policies/WhenLoggedInThenUpdateAppSession";
import LoginByEmailHandler from "../../../domain/auth/handlers/LoginByEmailHandler";
import LoginByEmail from "../../../domain/auth/commands/LoginByEmail";
import type AuthService from "../../../domain/auth/interfaces/services/AuthService";
import type PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import LoginByToken from "../../../domain/auth/commands/LoginByToken";
import LoginByTokenHandler from "../../../domain/auth/handlers/LoginByTokenHandler";
import BusImpl from "../../../domain/utils/BusImpl";
import WhenAppStartedThenTryLoginByToken from "../../../domain/auth/policies/WhenAppStartedThenTryLoginByToken";
import LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository";
import FailedLoginByToken from "../../../domain/auth/events/FailedLoginByToken";
import WhenFailedLoginByTokenThenUpdateAppSession
    from "../../../domain/auth/policies/WhenFailedLoginByTokenThenUpdateAppSession";
import LogoutHandler from "../../../domain/auth/handlers/LogoutHandler";
import Logout from "../../../domain/auth/commands/Logout";
import SignUpByEmail from "../../../domain/auth/commands/SignUpByEmail";
import SignUpByEmailHandler from "../../../domain/auth/handlers/SignUpByEmailHandler";
import WhenUserSignedUpThenUpdateAppSession from "../../../domain/auth/policies/WhenUserSignedUpThenUpdateAppSession";
import UserSignedUpByEmail from "../../../domain/auth/events/UserSignedUpByEmail";
import UpdatedUserPhoto from "../../../domain/auth/events/UpdatedUserPhoto";
import WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession
    from "../../../domain/auth/policies/WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession";
import UpdateUserInfoInAppSession from "../../../domain/auth/commands/UpdateUserInfoInAppSession";
import UpdateUserInfoInAppSessionHandler from "../../../domain/auth/handlers/UpdateUserInfoInAppSessionHandler";
import UploadNewUserPhotoHandler from "../../../domain/auth/handlers/UploadNewUserPhotoHandler.ts";
import UploadNewUserPhoto from "../../../domain/auth/commands/UploadNewUserPhoto.ts";

class DomainModule {

    public readonly appSession: AppSession;
    public readonly bus = new BusImpl();

    public constructor(
        private readonly localAuthData: LocalAuthDataRepository,
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService
    ) {
        this.appSession = new AppSessionImpl(
            this.bus
        );

        this.registerPolicies();
        this.registerCommandHandlers();
        this.bus.publish(new AppStarted());
    }

    private registerPolicies = (): void => {

        console.log("AppStarted", AppStarted)

        const policiesByEvents = {
            [AppStarted.type]: [
                new WhenAppStartedThenTryLoginByToken(this.localAuthData, this.bus),
            ],
            [PersonalInfoDidChange.type]: [
                new WhenPersonalInfoChangedUpdateAppSession(this.appSession)
            ],
            [UserLoggedOut.type]: [
                new WhenLogoutThenUpdateAppSession(this.appSession)
            ],
            [UserLoggedIn.type]: [
                new WhenLoggedInThenUpdateAppSession(this.appSession)
            ],
            [FailedLoginByToken.type]: [
                new WhenFailedLoginByTokenThenUpdateAppSession(this.appSession)
            ],
            [UserSignedUpByEmail.type]: [
                new WhenUserSignedUpThenUpdateAppSession(this.appSession)
            ],
            [UpdatedUserPhoto.type]: [
                new WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession(this.appSession, this.bus)
            ]
        };


        this.bus.subscribe(async event => {

            const policies = policiesByEvents[event.type];
            if (!policies) {
                return;
            }

            policies.forEach(policy => {
                console.log("%cDOMAIN/POLICY: ", "color: #FF33FF; font-weight: bold;", policy.type);
                policy.execute(event)
            });
        });
    }

    private registerCommandHandlers = (): void => {

        const handlersByCommands = {
            [LoginByEmail.type]: new LoginByEmailHandler(this.authService, this.userInfoService, this.bus),
            [LoginByToken.type]: new LoginByTokenHandler(this.authService, this.bus),
            [Logout.type]: new LogoutHandler(this.authService, this.bus),
            [SignUpByEmail.type]: new SignUpByEmailHandler(this.authService, this.userInfoService, this.bus),
            [UpdateUserInfoInAppSession.type]: new UpdateUserInfoInAppSessionHandler(this.appSession, this.userInfoService, this.bus),
            [UploadNewUserPhoto.type]: new UploadNewUserPhotoHandler(this.userInfoService, this.bus)
        };

        this.bus.subscribe(async command => {

            const handler = handlersByCommands[command.type];
            if (!handler) {
                return;
            }

            console.log(`%cDOMAIN/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
            handler.execute(command);
        });
    }
}

export default DomainModule;