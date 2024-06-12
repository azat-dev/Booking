import AppSession from "../entities/AppSession";
import UpdatedUserPhoto from "../events/personal-info/UpdatedUserPhoto.ts";
import AppSessionAuthenticated from "../entities/AppSessionAuthenticated";
import Policy from "../../utils/Policy";

class WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession extends Policy {

    public constructor(
        private readonly updateUserInfoInAppSession: () => void,
        private readonly appSession: AppSession
    ) {
        super();
    }

    public execute = async (event: UpdatedUserPhoto): Promise<void> => {

        const currentState = this.appSession.state.value;
        const isAuthenticated = currentState instanceof AppSessionAuthenticated;

        if (!isAuthenticated) {
            return;
        }

        this.updateUserInfoInAppSession();
    }
}

export default WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession;