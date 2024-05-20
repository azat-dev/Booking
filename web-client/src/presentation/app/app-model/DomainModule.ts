import AppSessionImpl from "../../../domain/auth/entities/AppSessionImpl";
import AppSession from "../../../domain/auth/entities/AppSession";
import AppStarted from "../../../domain/auth/events/AppStarted";
import PersonalInfoDidChange from "../../../domain/auth/events/PersonalInfoDidChange";
import UpdateAppSessionOnPersonalInfoChange from "../../../domain/auth/policies/UpdateAppSessionOnPersonalInfoChange";
import UserLoggedOut from "../../../domain/auth/events/UserLoggedOut";
import UpdateAppSessionOnLogout from "../../../domain/auth/policies/UpdateAppSessionOnLogout";
import UserLoggedIn from "../../../domain/auth/events/UserLoggedIn";
import UpdateAppSessionOnLogIn from "../../../domain/auth/policies/UpdateAppSessionOnLogIn";
import LoginByEmailHandler from "../../../domain/auth/handlers/LoginByEmailHandler";
import LoginByEmail from "../../../domain/auth/commands/LoginByEmail";
import type AuthService from "../../../domain/auth/interfaces/services/AuthService";
import type PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import LoginByToken from "../../../domain/auth/commands/LoginByToken";
import LoginByTokenHandler from "../../../domain/auth/handlers/LoginByTokenHandler";
import BusImpl from "../../../domain/utils/BusImpl";
import LoginByTokenOnAppStarted from "../../../domain/auth/policies/LoginByTokenOnAppStarted";
import LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository";
import FailedLoginByToken from "../../../domain/auth/events/FailedLoginByToken";
import UpdateAppSessionOnFailedLoginByToken from "../../../domain/auth/policies/UpdateAppSessionOnFailedLoginByToken";

class DomainModule {

    public readonly appSession: AppSession;
    public readonly bus = new BusImpl();

    public constructor(
        private readonly localAuthData: LocalAuthDataRepository,
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService
    ) {
        this.appSession = new AppSessionImpl();

        this.registerPolicies();
        this.registerCommandHandlers();
        this.bus.publish(new AppStarted());
    }

    private registerPolicies = (): void => {

        const policiesByEvents = {
            [AppStarted.name]: [
                new LoginByTokenOnAppStarted(this.localAuthData, this.bus),
            ],
            [PersonalInfoDidChange.name]: [
                new UpdateAppSessionOnPersonalInfoChange(this.appSession)
            ],
            [UserLoggedOut.name]: [
                new UpdateAppSessionOnLogout(this.appSession)
            ],
            [UserLoggedIn.name]: [
                new UpdateAppSessionOnLogIn(this.appSession)
            ],
            [FailedLoginByToken.name]: [
                new UpdateAppSessionOnFailedLoginByToken(this.appSession)
            ]
        };


        this.bus.subscribe(async event => {

            const policies = policiesByEvents[event.type];
            if (!policies) {
                return;
            }

            policies.forEach(policy => {
                console.log("Execute Domain Policy", policy.type, policy, event);
                policy.execute(event)
            });
        });
    }

    private registerCommandHandlers = (): void => {

        const handlersByCommands = {
            [LoginByEmail.name]: new LoginByEmailHandler(this.authService, this.userInfoService, this.bus),
            [LoginByToken.name]: new LoginByTokenHandler(this.authService, this.bus)
        };

        this.bus.subscribe(async command => {

            const handler = handlersByCommands[command.type];
            if (!handler) {
                return;
            }

            console.log("Execute Domain Handler", handler.type, handler, command);
            handler.execute(command);
        });
    }
}

export default DomainModule;