import UserId from "../../../values/UserId";
import Email from "../../../values/Email";
import FullName from "../FullName";
import Avatar from "../../../values/Avatar";

class PersonalUserInfo {
    public constructor(
        public readonly id: UserId,
        public readonly email: Email,
        public readonly fullName: FullName,
        public readonly photoUrl: Avatar | null
    ) {
    }
}

export default PersonalUserInfo;