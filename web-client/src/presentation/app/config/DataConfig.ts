import LocalAuthDataRepositoryImpl from "../../../data/auth/repositories/LocalAuthDataRepositoryImpl.ts";
import {Configuration, DefaultApi} from "../../../data/API";
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

    public api = singleton(
        () => {
            return new DefaultApi(this.apiConfig());
        }
    )
    public localAuthService = singleton(
        (): AuthService => {
            return new AuthServiceImpl(this.api(), this.localAuthDataRepository());
        }
    )
    public userInfoService = singleton(
        (): PersonalUserInfoService => {
            return new UserInfoServiceImpl(this.api());
        }
    )

    public constructor(
        private readonly baseApiUrl: string
    ) {
    }
}

export default DataConfig;