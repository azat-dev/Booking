import UserId from "../../values/UserId";
import AccessToken from "./AccessToken";

export interface LocalAuthData {
    accessToken: AccessToken;
    userId: UserId;
}

export default interface LocalAuthDataRepository {
    put(data: LocalAuthData): Promise<void>;
    get(): Promise<LocalAuthData | null>;
    clear(): Promise<void>;
}
