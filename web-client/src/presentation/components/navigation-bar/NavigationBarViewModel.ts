import CurrentSessionStore from "../../../domain/auth/CurrentSession/CurrentSessionStore";
import Subject from "../../utils/binding/Subject";
import value from "../../utils/binding/value";
import ProfileButtonAnonymousViewModel from "./profile-button-anonymous/ProfileButtonAnonymousViewModel";

export type ProfileButtonPresentation = {
    type: "anonymous";
    vm: ProfileButtonAnonymousViewModel;
};

class NavigationBarViewModel {
    public profileButton: Subject<ProfileButtonPresentation | null>;

    public constructor(
        private currentSessionStore: CurrentSessionStore,
        private makeAnonymousProfileButton: () => ProfileButtonAnonymousViewModel
    ) {
        this.profileButton = value(null);
        currentSessionStore.current.listen(this.updateProfileButton);
        this.updateProfileButton();
    }

    private updateProfileButton = () => {
        const session = this.currentSessionStore.current.value;

        if (session.type === "anonymous") {
            this.profileButton.set({
                type: "anonymous",
                vm: this.makeAnonymousProfileButton(),
            });
        } else {
            this.profileButton.set(null);
        }
    };
}

export default NavigationBarViewModel;
