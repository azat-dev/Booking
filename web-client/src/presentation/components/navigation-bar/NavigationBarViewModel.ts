import ProfileButtonViewModel from "./profile-button/ProfileButtonVM";


class NavigationBarViewModel {

    public constructor(
        public readonly profileButton: ProfileButtonViewModel
    ) {
    }
}

export default NavigationBarViewModel;
