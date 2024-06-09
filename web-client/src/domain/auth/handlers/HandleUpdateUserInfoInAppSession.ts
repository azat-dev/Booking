import Handler from "../../utils/Handler";
import UpdateUserInfoInAppSession from "../commands/UpdateUserInfoInAppSession";
import AppSession from "../entities/AppSession";
import AppSessionAuthenticated from "../entities/AppSessionAuthenticated";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import Bus from "../../utils/Bus";
import FailedUpdateUserInfoInAppSession from "../events/FailedUpdateUserInfoInAppSession";

class HandleUpdateUserInfoInAppSession extends Handler {

    public constructor(
        private readonly appSession: AppSession,
        private readonly infoService: PersonalUserInfoService,
        private readonly bus: Bus
    ) {
        super();
    }

    public execute = async (command: UpdateUserInfoInAppSession): Promise<void> => {
        const session = this.appSession.state.value;
        const isAuthenticated = session instanceof AppSessionAuthenticated;

        if (!isAuthenticated) {
            return;
        }

        try {
            const info = await this.infoService.getUserInfo(session.userInfo.value.id);
            session.updateUserInfo(info);
        } catch (e: any) {
            this.bus.publish(new FailedUpdateUserInfoInAppSession(e).withSender(command.senderId));
        }
    }
}

export default HandleUpdateUserInfoInAppSession;