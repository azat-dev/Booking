import AppSession from "../../../domain/auth/entities/AppSession";
import WhenPersonalInfoChangedUpdateAppSession
    from "../../../domain/auth/policies/WhenPersonalInfoChangedUpdateAppSession";
import WhenLogoutThenUpdateAppSession from "../../../domain/auth/policies/WhenLogoutThenUpdateAppSession";
import WhenLoggedInThenUpdateAppSession from "../../../domain/auth/policies/WhenLoggedInThenUpdateAppSession";
import WhenAppStartedThenTryLoginByToken from "../../../domain/auth/policies/WhenAppStartedThenTryLoginByToken";
import LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository";
import WhenFailedLoginByTokenThenUpdateAppSession
    from "../../../domain/auth/policies/WhenFailedLoginByTokenThenUpdateAppSession";
import WhenUserSignedUpThenUpdateAppSession from "../../../domain/auth/policies/WhenUserSignedUpThenUpdateAppSession";
import WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession
    from "../../../domain/auth/policies/WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession";
import AppStarted from "../../../domain/auth/events/AppStarted.ts";
import PersonalInfoDidChange from "../../../domain/auth/events/PersonalInfoDidChange.ts";
import UserLoggedOut from "../../../domain/auth/events/UserLoggedOut.ts";
import UserLoggedIn from "../../../domain/auth/events/UserLoggedIn.ts";
import FailedLoginByToken from "../../../domain/auth/events/FailedLoginByToken.ts";
import UserSignedUpByEmail from "../../../domain/auth/events/UserSignedUpByEmail.ts";
import UpdatedUserPhoto from "../../../domain/auth/events/UpdatedUserPhoto.ts";
import Bus from "../../../domain/utils/Bus.ts";
import Policy from "../../../domain/utils/Policy.ts";

class DomainPoliciesConfig {

    private readonly policiesByEvents: Record<string, Policy[]>;

    public constructor(
        public readonly appSession: AppSession,
        private readonly localAuthData: LocalAuthDataRepository,
        private readonly bus: Bus
    ) {

        this.policiesByEvents = {
            [AppStarted.name]: [
                new WhenAppStartedThenTryLoginByToken(this.localAuthData, this.bus),
            ],
            [PersonalInfoDidChange.name]: [
                new WhenPersonalInfoChangedUpdateAppSession(this.appSession)
            ],
            [UserLoggedOut.name]: [
                new WhenLogoutThenUpdateAppSession(this.appSession)
            ],
            [UserLoggedIn.name]: [
                new WhenLoggedInThenUpdateAppSession(this.appSession)
            ],
            [FailedLoginByToken.name]: [
                new WhenFailedLoginByTokenThenUpdateAppSession(this.appSession)
            ],
            [UserSignedUpByEmail.name]: [
                new WhenUserSignedUpThenUpdateAppSession(this.appSession)
            ],
            [UpdatedUserPhoto.name]: [
                new WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession(this.appSession, this.bus)
            ]
        };
    }

    public getForEvent = (event: any): Policy[] => {
        return this.policiesByEvents[event.constructor.name];
    }
}

export default DomainPoliciesConfig;