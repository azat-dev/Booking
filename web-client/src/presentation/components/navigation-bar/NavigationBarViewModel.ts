import CurrentSessionStore from "../../../domain/auth/CurrentSession/CurrentSessionStore";
import Subject from "../../utils/binding/Subject";
import value from "../../utils/binding/value";
import ProfileButtonAnonymousViewModel from "./profile-button-anonymous/ProfileButtonAnonymousViewModel";
import ProfileButtonAuthenticatedViewModel from "./profile-button-authenticated/ProfileButtonAuthenticatedViewModel";

export type ProfileButtonPresentation =
    | {
          type: "anonymous";
          vm: ProfileButtonAnonymousViewModel;
      }
    | {
          type: "authenticated";
          vm: ProfileButtonAuthenticatedViewModel;
      };

class NavigationBarViewModel {
    public profileButton: Subject<ProfileButtonPresentation | null>;

    public constructor(
        private currentSessionStore: CurrentSessionStore,
        private makeAnonymousProfileButton: () => ProfileButtonAnonymousViewModel,
        private makeAuthenticatedProfileButton: () => ProfileButtonAuthenticatedViewModel
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
            this.profileButton.set({
                type: "authenticated",
                vm: this.makeAuthenticatedProfileButton(),
            });
        }
    };
}

export default NavigationBarViewModel;
