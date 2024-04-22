export default interface TokensRepository {
    putAccessToken(accessToken: string): Promise<void>;
    getAccessToken(): Promise<string>;
    clear(): Promise<void>;
}
