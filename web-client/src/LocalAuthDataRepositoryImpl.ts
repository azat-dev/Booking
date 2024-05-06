import AccessToken from "./domain/auth/interfaces/repositories/AccessToken";
import LocalAuthDataRepository, {
    LocalAuthData,
} from "./domain/auth/interfaces/repositories/LocalAuthDataRepository";
import UserId from "./domain/auth/values/UserId";

export default class LocalAuthDataRepositoryImpl
    implements LocalAuthDataRepository
{
    private static AUTH_DATA_KEY = "authKeys";

    public constructor(private localStorage = window.localStorage) {}

    public put = async (data: LocalAuthData): Promise<void> => {
        const rawData = {
            userId: data.userId.val,
            accessToken: data.accessToken.val,
        };

        this.localStorage.setItem(
            LocalAuthDataRepositoryImpl.AUTH_DATA_KEY,
            JSON.stringify(rawData)
        );
    };

    public get = async (): Promise<LocalAuthData | null> => {
        const encodedData =
            this.localStorage.getItem(
                LocalAuthDataRepositoryImpl.AUTH_DATA_KEY
            ) ?? null;

        if (!encodedData) {
            return null;
        }

        const parsedData = JSON.parse(encodedData);
        return {
            userId: new UserId(parsedData.userId),
            accessToken: new AccessToken(parsedData.accessToken),
        };
    };

    public clear = async (): Promise<void> => {
        this.localStorage.removeItem(LocalAuthDataRepositoryImpl.AUTH_DATA_KEY);
    };
}
