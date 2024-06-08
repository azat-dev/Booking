import LocalAuthDataRepositoryImpl from "../../../data/auth/repositories/LocalAuthDataRepositoryImpl.ts";
import {
    CommandsLoginApi,
    CommandsSignUpApi,
    CommandsUpdateUserPhotoApi,
    Configuration,
    QueriesCurrentUserApi
} from "../../../data/api/identity";
import AuthServiceImpl from "../../../data/auth/services/AuthServiceImpl.ts";
import PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService.ts";
import UserInfoServiceImpl from "../../../data/auth/services/UserInfoServiceImpl.ts";
import LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository.ts";
import AuthService from "../../../domain/auth/interfaces/services/AuthService.ts";
import singleton from "../../../utils/singleton.ts";

class DataConfig {

    public localAuthDataRepository = singleton(
        (): LocalAuthDataRepository => {
            return new LocalAuthDataRepositoryImpl();
        }
    )
    public apiConfig = singleton(
        () => {

            const localAuthData = this.localAuthDataRepository();

            return new Configuration({
                basePath: this.baseApiUrl,
                accessToken: async () => {
                    const authData = await localAuthData.get();
                    return authData?.accessToken.val ?? "";
                }
            });
        }
    );

    public currentUserApi = singleton(
        () => {
            return new QueriesCurrentUserApi(this.apiConfig());
        }
    )

    public updateUserPhotoApi = singleton(
        () => {
            return new CommandsUpdateUserPhotoApi(this.apiConfig());
        }
    )

    public loginApi = singleton(
        () => {
            return new CommandsLoginApi(this.apiConfig());
        }
    )

    public signUpApi = singleton(
        () => {
            return new CommandsSignUpApi(this.apiConfig());
        }
    )

    public localAuthService = singleton(
        (): AuthService => {
            return new AuthServiceImpl(
                this.loginApi(),
                this.signUpApi(),
                this.currentUserApi(),
                this.localAuthDataRepository()
            );
        }
    )
    public userInfoService = singleton(
        (): PersonalUserInfoService => {
            return new UserInfoServiceImpl(
                this.currentUserApi(),
                this.updateUserPhotoApi()
            );
        }
    )

    public constructor(
        private readonly baseApiUrl: string
    ) {
    }
}

export default DataConfig;