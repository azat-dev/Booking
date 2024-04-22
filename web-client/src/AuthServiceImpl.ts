import AuthService, {
    AuthenticationByEmailResult,
} from "./domain/auth/CurrentSession/Session/AuthService";
import Email from "./domain/auth/values/Email";
import Password from "./domain/auth/values/Password";
import User from "./domain/auth/values/User";
import UserId from "./domain/auth/values/UserId";

class AuthServiceImpl implements AuthService {
    private testUser: User = {
        id: new UserId("some-id"),
        email: new Email("some@email.com"),
        name: "Some Name",
        avatar: null,
    };

    public constructor() {}

    public authenticateByEmail = async (
        email: Email,
        password: Password
    ): Promise<AuthenticationByEmailResult> => {
        return {
            accessToken: "some-access",
            userInfo: this.testUser,
        };
    };

    public authenticateByToken = async (token: string): Promise<User> => {
        return this.testUser;
    };

    public logout = async (): Promise<void> => {
        return;
    };
}

export default AuthServiceImpl;
