import AuthService, {
    AuthenticateByEmailData,
    AuthenticationByEmailResult,
    SignUpByEmailResult, WrongCredentialsError,
} from "../../../domain/auth/CurrentSession/Session/AuthService";
import FirstName from "../../../domain/auth/CurrentSession/Session/FirstName";
import FullName from "../../../domain/auth/CurrentSession/Session/FullName";
import LastName from "../../../domain/auth/CurrentSession/Session/LastName";
import SignUpByEmailData from "../../../domain/auth/CurrentSession/Session/SignUpByEmailData";
import AccessToken from "../../../domain/auth/interfaces/repositories/AccessToken";
import LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository";
import Email from "../../../domain/auth/values/Email";
import UserInfo from "../../../domain/auth/values/User";
import UserId from "../../../domain/auth/values/UserId";
import {
    DefaultApi,
    ResponseError,
    UserWithSameEmailAlreadyExistsError,
    ValidationError,
} from "../../API";

class AuthServiceImpl implements AuthService {
    private testUser: UserInfo = {
        id: new UserId("some-id"),
        email: new Email("some@email.com"),
        fullName: new FullName(new FirstName("Some"), new LastName("Name")),
        avatar: null,
    };

    public constructor(
        private readonly api: DefaultApi,
        private readonly localAuthDataRepository: LocalAuthDataRepository
    ) {}

    public authenticateByEmail = async (
        data: AuthenticateByEmailData
    ): Promise<AuthenticationByEmailResult> => {
        try {
            const result = await this.api.apiPublicAuthTokenPost({
                authenticateByEmailRequest: {
                    email: data.email.value,
                    password: data.password,
                },
            });

            await this.localAuthDataRepository.put({
                userId: new UserId(result.userId),
                accessToken: new AccessToken(result.tokens.access),
            });

            return {
                userId: new UserId(result.userId),
                tokens: result.tokens,
            };
        } catch (e) {

            if (e instanceof ResponseError) {
                if (e.response.status === 401) {
                    throw new WrongCredentialsError();
                }

                throw e;
            }

            throw e;
        }
    };

    public authenticateByToken = async (token: string): Promise<UserInfo> => {
        return this.testUser;
    };

    public logout = async (): Promise<void> => {
        await this.localAuthDataRepository.clear();
    };

    public signUpByEmail = async (
        data: SignUpByEmailData
    ): Promise<SignUpByEmailResult> => {
        try {
            const result = await this.api.apiPublicAuthSignUpPost({
                signUpByEmailRequest: {
                    email: data.email.value,
                    password: data.password.value,
                    fullName: {
                        firstName: data.fullName.firstName.value,
                        lastName: data.fullName.lastName.value,
                    },
                },
            });

            await this.localAuthDataRepository.put({
                userId: new UserId(result.userId),
                accessToken: new AccessToken(result.tokens.access),
            });

            return {
                userId: new UserId(result.userId),
                tokens: result.tokens,
            };
        } catch (e) {
            if (e instanceof ResponseError) {
                if (e.response.status === 400) {
                    const error = (await e.response.json()) as ValidationError;
                    throw error;
                }

                if (e.response.status) {
                    const error =
                        (await e.response.json()) as UserWithSameEmailAlreadyExistsError;
                    throw error;
                }
            }

            console.error(e);
            throw e;
        }
    };
}

export default AuthServiceImpl;
