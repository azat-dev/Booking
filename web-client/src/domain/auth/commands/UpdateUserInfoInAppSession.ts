import AppSession from "../entities/AppSession";
import AppSessionAuthenticated from "../entities/AppSessionAuthenticated";
import PersonalUserInfoService from "../interfaces/services/PersonalUserInfoService";
import {Emit} from "../../utils/Bus";
import FailedUpdateUserInfoInAppSession from "../events/FailedUpdateUserInfoInAppSession";

class UpdateUserInfoInAppSession {

    public constructor(
        private readonly appSession: AppSession,
        private readonly infoService: PersonalUserInfoService,
        private readonly emit: Emit
    ) {
    }

    public execute = async (): Promise<void> => {
        const session = this.appSession.state.value;
        const isAuthenticated = session instanceof AppSessionAuthenticated;

        if (!isAuthenticated) {
            return;
        }

        try {
            const info = await this.infoService.getUserInfo(session.userInfo.value.id);
            session.updateUserInfo(info);
        } catch (e: any) {
            const errMsg = new FailedUpdateUserInfoInAppSession(e);
            this.emit(errMsg);
            throw errMsg;
        }
    }
}

export default UpdateUserInfoInAppSession;