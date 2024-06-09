import {
    CommandsLoginApi,
    CommandsSignUpApi,
    CommandsUpdateUserPhotoApi,
    Configuration as UsersConfiguration,
    QueriesCurrentUserApi
} from "../../../../data/api/identity";
import AuthServiceImpl from "../../../../data/auth/services/AuthServiceImpl.ts";
import PersonalUserInfoService from "../../../../domain/auth/interfaces/services/PersonalUserInfoService.ts";
import UserInfoServiceImpl from "../../../../data/auth/services/UserInfoServiceImpl.ts";
import LocalAuthDataRepository from "../../../../domain/auth/interfaces/repositories/LocalAuthDataRepository.ts";
import AuthService from "../../../../domain/auth/interfaces/services/AuthService.ts";
import singleton from "../../../../utils/singleton.ts";

class IdentityDataConfig {

    public userInfoService = singleton(
        (): PersonalUserInfoService => {
            return new UserInfoServiceImpl(
                this.currentUserApi(),
                this.updateUserPhotoApi()
            );
        }
    );
    public localAuthService = singleton(
        (): AuthService => {
            return new AuthServiceImpl(
                this.loginApi(),
                this.signUpApi(),
                this.currentUserApi(),
                this.localAuthData
            );
        }
    );
    private usersApiConfig = singleton(
        () => {

            return new UsersConfiguration({
                basePath: this.baseApiUrl,
                accessToken: async () => {
                    const authData = await this.localAuthData.get();
                    return authData?.accessToken.val ?? "";
                }
            });
        }
    );
    public currentUserApi = singleton(
        () => {
            return new QueriesCurrentUserApi(this.usersApiConfig());
        }
    );
    public updateUserPhotoApi = singleton(
        () => {
            return new CommandsUpdateUserPhotoApi(this.usersApiConfig());
        }
    );
    public loginApi = singleton(
        () => {
            return new CommandsLoginApi(this.usersApiConfig());
        }
    );
    public signUpApi = singleton(
        () => {
            return new CommandsSignUpApi(this.usersApiConfig());
        }
    );

    public constructor(
        private readonly baseApiUrl: string,
        private readonly localAuthData: LocalAuthDataRepository
    ) {
    }
}

export default IdentityDataConfig;