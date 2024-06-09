import LocalAuthDataRepository from "../../../../domain/auth/interfaces/repositories/LocalAuthDataRepository.ts";
import singleton from "../../../../utils/singleton.ts";
import {
    CommandsListingsPhotoApi,
    CommandsModificationsApi as CommandsListingsModificationsApi,
    Configuration as ListingsConfiguration,
    QueriesPrivateApi as QueriesListingsPrivateApi
} from "../../../../data/api/listings";

class ListingsDataConfig {

    private apiConfig = singleton(
        () => {


            return new ListingsConfiguration({
                basePath: this.baseApiUrl,
                accessToken: async () => {
                    const authData = await this.localAuthData.get();
                    return authData?.accessToken.val ?? "";
                }
            });
        }
    );

    public commandsListingsPhotoApi = singleton(
        () => {
            return new CommandsListingsPhotoApi(this.apiConfig());
        }
    )

    public listingsModificationsApi = singleton(
        () => {
            return new CommandsListingsModificationsApi(this.apiConfig());
        }
    )

    public listingsPrivateQueriesApi = singleton(
        () => {
            return new QueriesListingsPrivateApi(this.apiConfig());
        }
    )

    public constructor(
        private readonly baseApiUrl: string,
        private readonly localAuthData: LocalAuthDataRepository
    ) {
    }
}

export default ListingsDataConfig;