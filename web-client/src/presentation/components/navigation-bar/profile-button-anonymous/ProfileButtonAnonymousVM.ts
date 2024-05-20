class ProfileButtonAnonymousVM {

    public static readonly TYPE = "ProfileButtonAnonymousVM";

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
