import CurrentSessionStore from "../../../../domain/auth/CurrentSession/CurrentSessionStore";
import ProfileButtonAnonymousViewModel from "../profile-button-anonymous/ProfileButtonAnonymousViewModel";
import ProfileButtonAuthenticatedViewModel from "../profile-button-authenticated/ProfileButtonAuthenticatedViewModel";
import value from "../../../utils/binding/value";
import SessionStatus from "../../../../domain/auth/CurrentSession/Session/SessionStatus";
import Subject from "../../../utils/binding/Subject";


export enum ProfileButtonPresentationType {
    ANONYMOUS,
    AUTHENTICATED,
    PROCESSING
}

export type ProfileButtonPresentation =
    | {
    type: ProfileButtonPresentationType.ANONYMOUS;
    vm: ProfileButtonAnonymousViewModel;
}
    | {
    type: ProfileButtonPresentationType.AUTHENTICATED;
    vm: ProfileButtonAuthenticatedViewModel;
} | {
    type: ProfileButtonPresentationType.PROCESSING
};

class ProfileButtonViewModel {

    public button: Subject<ProfileButtonPresentation>;

    public constructor(
        private currentSessionStore: CurrentSessionStore,
        private makeAnonymousProfileButton: () => ProfileButtonAnonymousViewModel,
        private makeAuthenticatedProfileButton: () => ProfileButtonAuthenticatedViewModel
    ) {
        this.button = value({type: ProfileButtonPresentationType.PROCESSING});
        currentSessionStore.current.listen(this.updateProfileButton);
        this.updateProfileButton();
    }

    private updateProfileButton = () => {
        const session = this.currentSessionStore.current.value;

        switch (session.type) {
            case SessionStatus.ANONYMOUS:
                this.button.set({
                    type: ProfileButtonPresentationType.ANONYMOUS,
                    vm: this.makeAnonymousProfileButton(),
                });
                return;

            case SessionStatus.AUTHENTICATED:
                this.button.set({
                    type: ProfileButtonPresentationType.AUTHENTICATED,
                    vm: this.makeAuthenticatedProfileButton(),
                });
                return;;

            case SessionStatus.PROCESSING:
                this.button.set({type: ProfileButtonPresentationType.PROCESSING})
                return;
        }
    };
}

export default ProfileButtonViewModel;
