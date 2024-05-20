import PersonalUserInfoService from "../../../domain/auth/interfaces/services/PersonalUserInfoService";
import {DefaultApi} from "../../API";
import PersonalUserInfo from "../../../domain/auth/values/PersonalUserInfo";
import Email from "../../../domain/auth/values/Email";
import UserId from "../../../domain/auth/values/UserId";
import FullName from "../../../domain/auth/values/FullName";
import FirstName from "../../../domain/auth/values/FirstName";
import LastName from "../../../domain/auth/values/LastName";

class UserInfoServiceImpl implements PersonalUserInfoService {

    public constructor(private api: DefaultApi) {
    }

    public getUserInfo = async (): Promise<PersonalUserInfo> => {
        const userInfo = await this.api.apiWithAuthUsersCurrentGet();
        return new PersonalUserInfo(
            UserId.fromString(userInfo.id),
            Email.dangerouslyCreate(userInfo.email),
            new FullName(
                FirstName.dangerouslyCreate(userInfo.fullName.firstName),
                LastName.dangerouslyCreate(userInfo.fullName.lastName)
            ),
            null
        );
    };
}

export default UserInfoServiceImpl;