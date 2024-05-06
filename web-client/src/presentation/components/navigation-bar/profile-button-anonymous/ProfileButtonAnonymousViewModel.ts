import CurrentSessionStore from "../../../../domain/auth/CurrentSession/CurrentSessionStore";

class ProfileButtonAnonymousViewModel {
    public constructor(
        private readonly onLogin: () => void,
        private readonly onSignUp: () => void
    ) {}

    public login = () => {
        this.onLogin();
    };

    public signUp = () => {
        this.onSignUp();
    };
}

export default ProfileButtonAnonymousViewModel;
