import AuthService, {
    AuthenticateByEmailData,
    AuthenticationByEmailResult,
    SignUpByEmailResult,
    WrongCredentialsError,
} from "../../../domain/auth/interfaces/services/AuthService";
import FirstName from "../../../domain/auth/values/FirstName";
import FullName from "../../../domain/auth/values/FullName";
import LastName from "../../../domain/auth/values/LastName";
import SignUpByEmailData from "../../../domain/auth/interfaces/services/SignUpByEmailData";
import AccessToken from "../../../domain/auth/interfaces/repositories/AccessToken";
import type LocalAuthDataRepository from "../../../domain/auth/interfaces/repositories/LocalAuthDataRepository";
import Email from "../../../domain/auth/values/Email";
import UserId from "../../../domain/auth/values/UserId";
import {
    CommandsLoginApi, CommandsSignUpApi,
    QueriesCurrentUserApi,
    ResponseError,
    UserWithSameEmailAlreadyExistsErrorDTO,
    ValidationErrorDTO,
} from "../../API";
import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import PhotoPath from "../../../domain/auth/values/PhotoPath.ts";


class AuthServiceImpl implements AuthService {
    public constructor(
        private readonly loginApi: CommandsLoginApi,
        private readonly signUpApi: CommandsSignUpApi,
        private readonly currentUserApi: QueriesCurrentUserApi,
        private readonly localAuthDataRepository: LocalAuthDataRepository
    ) {
    }

    public authenticateByEmail = async (
        data: AuthenticateByEmailData
    ): Promise<AuthenticationByEmailResult> => {
        try {
            const result = await this.loginApi.loginByEmail({
                loginByEmailRequestBody: {
                    email: data.email.value,
                    password: data.password,
                },
            });

            await this.localAuthDataRepository.put({
                userId: UserId.fromString(result.userId),
                accessToken: new AccessToken(result.tokens.access),
            });

            return {
                userId: UserId.fromString(result.userId),
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

    public authenticateByToken = async (token: AccessToken): Promise<PersonalUserInfo> => {
        const userInfo = await this.currentUserApi.getCurrentUser();
        const photo = userInfo.photo?.url ? new PhotoPath(userInfo.photo?.url) : null;

        return new PersonalUserInfo(
            UserId.fromString(userInfo.id),
            Email.dangerouslyCreate(userInfo.email),
            new FullName(
                FirstName.dangerouslyCreate(userInfo.fullName.firstName),
                LastName.dangerouslyCreate(userInfo.fullName.lastName)
            ),
            photo
        )
    };

    public logout = async (): Promise<void> => {
        await this.localAuthDataRepository.clear();
    };

    public signUpByEmail = async (
        data: SignUpByEmailData
    ): Promise<SignUpByEmailResult> => {
        try {
            const result = await this.signUpApi.signUpByEmail({
                signUpByEmailRequestBody: {
                    email: data.email.value,
                    password: data.password.value,
                    fullName: {
                        firstName: data.fullName.firstName.value,
                        lastName: data.fullName.lastName.value,
                    },
                },
            });

            await this.localAuthDataRepository.put({
                userId: UserId.fromString(result.userId),
                accessToken: new AccessToken(result.tokens.access),
            });

            return {
                userId: UserId.fromString(result.userId),
                tokens: result.tokens,
            };
        } catch (e) {
            if (e instanceof ResponseError) {
                if (e.response.status === 400) {
                    const error = (await e.response.json()) as ValidationErrorDTO;
                    throw error;
                }

                if (e.response.status) {
                    const error =
                        (await e.response.json()) as UserWithSameEmailAlreadyExistsErrorDTO;
                    throw error;
                }
            }

            console.error(e);
            throw e;
        }
    };
}

export default AuthServiceImpl;
