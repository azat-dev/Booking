class ProfileButtonAnonymousVM {

    public static readonly TYPE = "PROFILE_BUTTON_ANONYMOUS";

    public constructor(
        private readonly onLogin: () => void,
        private readonly onSignUp: () => void
    ) {
    }

    public get type() {
        return (this.constructor as any).TYPE;
    }

    public login = () => {
        this.onLogin();
    };

    public signUp = () => {
        this.onSignUp();
    };
}

export default ProfileButtonAnonymousVM;
