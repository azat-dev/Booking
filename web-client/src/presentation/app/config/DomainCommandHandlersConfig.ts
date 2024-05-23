import AppSession from "../../../domain/auth/entities/AppSession";
import LoginByEmailHandler from "../../../domain/auth/handlers/LoginByEmailHandler";
import LoginByEmail from "../../../domain/auth/commands/LoginByEmail";
import type AuthService from "../../../domain/auth/interfaces/services/AuthService";
import type PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import LoginByToken from "../../../domain/auth/commands/LoginByToken";
import LoginByTokenHandler from "../../../domain/auth/handlers/LoginByTokenHandler";
import LogoutHandler from "../../../domain/auth/handlers/LogoutHandler";
import Logout from "../../../domain/auth/commands/Logout";
import SignUpByEmail from "../../../domain/auth/commands/SignUpByEmail";
import SignUpByEmailHandler from "../../../domain/auth/handlers/SignUpByEmailHandler";
import UpdateUserInfoInAppSession from "../../../domain/auth/commands/UpdateUserInfoInAppSession";
import UpdateUserInfoInAppSessionHandler from "../../../domain/auth/handlers/UpdateUserInfoInAppSessionHandler";
import UploadNewUserPhotoHandler from "../../../domain/auth/handlers/UploadNewUserPhotoHandler";
import UploadNewUserPhoto from "../../../domain/auth/commands/UploadNewUserPhoto";
import DomainPoliciesConfig from "./DomainPoliciesConfig";
import Bus from "../../../domain/utils/Bus";
import Handler from "../../../domain/utils/Handler.ts";
import Command from "../../../domain/utils/Command.ts";

class DomainCommandHandlersConfig {

    private readonly handlersByCommands: Record<string, Handler>;

    public constructor(
        public readonly appSession: AppSession,
        public readonly bus: Bus,
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly policies: DomainPoliciesConfig
    ) {
        this.handlersByCommands = {
            [LoginByEmail.name]: new LoginByEmailHandler(this.authService, this.userInfoService, this.bus),
            [LoginByToken.name]: new LoginByTokenHandler(this.authService, this.bus),
            [Logout.name]: new LogoutHandler(this.authService, this.bus),
            [SignUpByEmail.name]: new SignUpByEmailHandler(this.authService, this.userInfoService, this.bus),
            [UpdateUserInfoInAppSession.name]: new UpdateUserInfoInAppSessionHandler(this.appSession, this.userInfoService, this.bus),
            [UploadNewUserPhoto.name]: new UploadNewUserPhotoHandler(this.userInfoService, this.bus)
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlersByCommands[command.constructor.name];
    }
}

export default DomainCommandHandlersConfig;