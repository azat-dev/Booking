import TokensRepository from "./domain/auth/interfaces/repositories/TokensRepository";

export default class LocalStorageTokensRepository implements TokensRepository {
    private static ACCESS_TOKEN_KEY = "accessToken";

    public constructor(private localStorage = window.localStorage) {}

    public putAccessToken = async (accessToken: string): Promise<void> => {
        this.localStorage.setItem(
            LocalStorageTokensRepository.ACCESS_TOKEN_KEY,
            accessToken
        );
    };

    public getAccessToken = async (): Promise<string | null> => {
        return (
            this.localStorage.getItem(
                LocalStorageTokensRepository.ACCESS_TOKEN_KEY
            ) ?? null
        );
    };

    public clear = async (): Promise<void> => {
        this.localStorage.removeItem(
            LocalStorageTokensRepository.ACCESS_TOKEN_KEY
        );
    };
}
