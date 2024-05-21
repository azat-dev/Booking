import AppSession from "../entities/AppSession";
import UpdatedUserPhoto from "../events/UpdatedUserPhoto";
import AppSessionAuthenticated from "../entities/AppSessionAuthenticated";
import Policy from "../../utils/Policy";
import Bus from "../../utils/Bus";
import UpdateUserInfoInAppSession from "../commands/UpdateUserInfoInAppSession";

class WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession extends Policy {

    public constructor(
        private readonly appSession: AppSession,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (event: UpdatedUserPhoto): Promise<void> => {

        const currentState = this.appSession.state.value;
        const isAuthenticated = currentState instanceof AppSessionAuthenticated;

        if (!isAuthenticated) {
            return;
        }

        this.bus.publish(new UpdateUserInfoInAppSession())
    }
}

export default WhenUpdatedUserPhotoThenUpdateUserInfoInAppSession;