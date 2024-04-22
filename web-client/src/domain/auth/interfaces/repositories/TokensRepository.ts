export default interface TokensRepository {
    putAccessToken(accessToken: string): Promise<void>;
    getAccessToken(): Promise<string | null>;
    clear(): Promise<void>;
}
