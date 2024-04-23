class NavigationBarViewModel {
    public constructor(private readonly onLogin: () => void) {}

    public login = () => {
        this.onLogin();
    };
}

export default NavigationBarViewModel;
