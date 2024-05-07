import ProfileButtonViewModel from "./profile-button/ProfileButtonViewModel";


class NavigationBarViewModel {

    public constructor(
        public readonly profileButton: ProfileButtonViewModel
    ) {
    }
}

export default NavigationBarViewModel;
