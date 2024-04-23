class NavigationBarViewModel {
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

export default NavigationBarViewModel;
