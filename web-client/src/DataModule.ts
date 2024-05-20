import LocalAuthDataRepositoryImpl from "./data/auth/repositories/LocalAuthDataRepositoryImpl";
import {Configuration, DefaultApi} from "./data/API";
import AuthServiceImpl from "./data/auth/services/AuthServiceImpl";
import PersonalUserInfoService from "./domain/auth/interfaces/services/PersonalUserInfoService";
import UserInfoServiceImpl from "./data/auth/services/UserInfoServiceImpl";
import LocalAuthDataRepository from "./domain/auth/interfaces/repositories/LocalAuthDataRepository";
import AuthService from "./domain/auth/interfaces/services/AuthService";

class DataModule {

    public constructor(
        private readonly baseApiUrl: string
    ) {
    }

    public localAuthDataRepository = (): LocalAuthDataRepository => {
        return new LocalAuthDataRepositoryImpl();
    }

    public apiConfig = () => {

        const localAuthData = this.localAuthDataRepository();

        return new Configuration({
            basePath: this.baseApiUrl,
            accessToken: async () => {
                const authData = await localAuthData.get();
                return authData?.accessToken.val ?? "";
            }
        });
    }

    public api = () => {
        return new DefaultApi(this.apiConfig());
    }

    public authService = (): AuthService => {
        return new AuthServiceImpl(this.api(), this.localAuthDataRepository());
    }

    public userInfoService = (): PersonalUserInfoService => {
        return new UserInfoServiceImpl(this.api());
    }
}

export default DataModule;