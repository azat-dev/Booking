import AppSession from "../../../../domain/auth/entities/AppSession.ts";
import HandleLoginByEmail from "../../../../domain/auth/handlers/HandleLoginByEmail.ts";
import LoginByEmail from "../../../../domain/auth/commands/LoginByEmail.ts";
import type AuthService from "../../../../domain/auth/interfaces/services/AuthService.ts";
import type PersonalUserInfoService from "../../../../domain/auth/interfaces/services/PersonalUserInfoService.ts";
import LoginByToken from "../../../../domain/auth/commands/LoginByToken.ts";
import HandleLoginByToken from "../../../../domain/auth/handlers/HandleLoginByToken.ts";
import HandleLogout from "../../../../domain/auth/handlers/HandleLogout.ts";
import Logout from "../../../../domain/auth/commands/Logout.ts";
import SignUpByEmail from "../../../../domain/auth/commands/SignUpByEmail.ts";
import HandleSignUpByEmail from "../../../../domain/auth/handlers/HandleSignUpByEmail.ts";
import UpdateUserInfoInAppSession from "../../../../domain/auth/commands/UpdateUserInfoInAppSession.ts";
import HandleUpdateUserInfoInAppSession from "../../../../domain/auth/handlers/HandleUpdateUserInfoInAppSession.ts";
import HandleUploadNewUserPhoto from "../../../../domain/auth/handlers/HandleUploadNewUserPhoto.ts";
import UploadNewUserPhoto from "../../../../domain/auth/commands/UploadNewUserPhoto.ts";
import Bus from "../../../../domain/utils/Bus.ts";
import Handler from "../../../../domain/utils/Handler.ts";
import Command from "../../../../domain/utils/Command.ts";
import CommandHandlersProvider from "./CommandHandlersProvider.ts";

class IdentityCommandHandlersProvider implements CommandHandlersProvider {

    private readonly handlersByCommands: Record<string, Handler>;

    public constructor(
        public readonly appSession: AppSession,
        public readonly bus: Bus,
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService
    ) {
        this.handlersByCommands = {
            [LoginByEmail.name]: new HandleLoginByEmail(this.authService, this.userInfoService, this.bus),
            [LoginByToken.name]: new HandleLoginByToken(this.authService, this.bus),
            [Logout.name]: new HandleLogout(this.authService, this.bus),
            [SignUpByEmail.name]: new HandleSignUpByEmail(this.authService, this.userInfoService, this.bus),
            [UpdateUserInfoInAppSession.name]: new HandleUpdateUserInfoInAppSession(this.appSession, this.userInfoService, this.bus),
            [UploadNewUserPhoto.name]: new HandleUploadNewUserPhoto(this.userInfoService, this.bus),
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlersByCommands[command.constructor.name];
    }
}

export default IdentityCommandHandlersProvider;