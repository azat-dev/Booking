import AppSession from "../../../../domain/auth/entities/AppSession.ts";
import WhenPersonalInfoChangedUpdateAppSession
    from "../../../../domain/auth/policies/WhenPersonalInfoChangedUpdateAppSession.ts";
import WhenLogoutThenUpdateAppSession from "../../../../domain/auth/policies/WhenLogoutThenUpdateAppSession.ts";
import WhenLoggedInThenUpdateAppSession from "../../../../domain/auth/policies/WhenLoggedInThenUpdateAppSession.ts";
import WhenAppStartedThenTryLoginByToken from "../../../../domain/auth/policies/WhenAppStartedThenTryLoginByToken.ts";
import LocalAuthDataRepository from "../../../../domain/auth/interfaces/repositories/LocalAuthDataRepository.ts";
import WhenFailedLoginByTokenThenUpdateAppSession
    from "../../../../domain/auth/policies/WhenFailedLoginByTokenThenUpdateAppSession.ts";
import WhenUserSignedUpThenUpdateAppSession
    from "../../../../domain/auth/policies/WhenUserSignedUpThenUpdateAppSession.ts";
import WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession
    from "../../../../domain/auth/policies/WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession.ts";
import AppStarted from "../../../../domain/auth/events/AppStarted.ts";
import PersonalInfoDidChange from "../../../../domain/auth/events/PersonalInfoDidChange.ts";
import UserLoggedOut from "../../../../domain/auth/events/UserLoggedOut.ts";
import UserLoggedIn from "../../../../domain/auth/events/login/UserLoggedIn.ts";
import FailedLoginByToken from "../../../../domain/auth/events/login/login-by-token/FailedLoginByToken.ts";
import UserSignedUpByEmail from "../../../../domain/auth/events/sign-up/UserSignedUpByEmail.ts";
import UpdatedUserPhoto from "../../../../domain/auth/events/personal-info/UpdatedUserPhoto.ts";
import Bus from "../../../../domain/utils/Bus.ts";
import Policy from "../../../../domain/utils/Policy.ts";
import AppEvent from "../../../../domain/utils/AppEvent.ts";
import PoliciesProvider from "./PoliciesProvider.ts";

class IdentityPolicies implements PoliciesProvider {

    private readonly policiesByEvents: Record<string, Policy[]>;

    public constructor(
        appSession: AppSession,
        localAuthData: LocalAuthDataRepository,
        bus: Bus
    ) {

        this.policiesByEvents = {
            [AppStarted.name]: [
                new WhenAppStartedThenTryLoginByToken(localAuthData, bus),
            ],
            [PersonalInfoDidChange.name]: [
                new WhenPersonalInfoChangedUpdateAppSession(appSession)
            ],
            [UserLoggedOut.name]: [
                new WhenLogoutThenUpdateAppSession(appSession)
            ],
            [UserLoggedIn.name]: [
                new WhenLoggedInThenUpdateAppSession(appSession)
            ],
            [FailedLoginByToken.name]: [
                new WhenFailedLoginByTokenThenUpdateAppSession(appSession)
            ],
            [UserSignedUpByEmail.name]: [
                new WhenUserSignedUpThenUpdateAppSession(appSession)
            ],
            [UpdatedUserPhoto.name]: [
                new WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession(appSession, bus)
            ]
        };
    }

    public getForEvent = (event: AppEvent): Policy[] => {
        return this.policiesByEvents[event.constructor.name];
    }
}

export default IdentityPolicies;