import AppSession from "../../../../domain/auth/entities/AppSession.ts";
import LoginByEmail from "../../../../domain/auth/commands/LoginByEmail.ts";
import type AuthService from "../../../../domain/auth/interfaces/services/AuthService.ts";
import type PersonalUserInfoService from "../../../../domain/auth/interfaces/services/PersonalUserInfoService.ts";
import LoginByToken from "../../../../domain/auth/commands/LoginByToken.ts";
import Logout from "../../../../domain/auth/commands/Logout.ts";
import SignUpByEmail from "../../../../domain/auth/commands/SignUpByEmail.ts";
import UpdateUserInfoInAppSession from "../../../../domain/auth/commands/UpdateUserInfoInAppSession.ts";

import {Emit} from "../../../../domain/utils/Bus.ts";
import UploadNewUserPhoto from "../../../../domain/auth/commands/UploadNewUserPhoto.ts";
import Email from "../../../../domain/auth/values/Email.ts";
import FullName from "../../../../domain/auth/values/FullName.ts";
import Password from "../../../../domain/auth/values/Password.ts";
import UserId from "../../../../domain/auth/values/UserId.ts";
import AccessToken from "../../../../domain/auth/interfaces/repositories/AccessToken.ts";

class IdentityCommands {

    public constructor(
        public readonly appSession: AppSession,
        private readonly authService: AuthService,
        private readonly userInfoService: PersonalUserInfoService,
        private readonly emit: Emit
    ) {
    }

    public loginByToken = (token: AccessToken) => {
        return new LoginByToken(this.authService, this.emit)
            .execute(token);
    }

    public loginByEmail = (email: Email, password: string) => {
        return new LoginByEmail(this.authService, this.userInfoService, this.emit)
            .execute(email, password);
    }

    public logout = () => {
        return new Logout(this.authService, this.emit)
            .execute();
    }

    public signUpByEmail = (fullName: FullName, email: Email, password: Password) => {
        return new SignUpByEmail(this.authService, this.userInfoService, this.emit)
            .execute(fullName, email, password);
    }

    public updateUserInfoInAppSession = () => {
        return new UpdateUserInfoInAppSession(this.appSession, this.userInfoService, this.emit)
            .execute();
    }

    public uploadNewUserPhoto = (userId: UserId, file: File) => {
        return new UploadNewUserPhoto(this.userInfoService, this.emit)
            .execute(userId, file);
    }
}

export default IdentityCommands;