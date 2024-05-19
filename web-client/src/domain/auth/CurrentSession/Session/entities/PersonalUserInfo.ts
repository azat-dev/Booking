import UserId from "../../../values/UserId";
import Email from "../../../values/Email";
import FullName from "../FullName";
import {PhotoPath} from "../../../values/PhotoPath";

class PersonalUserInfo {
    public constructor(
        public readonly id: UserId,
        public readonly email: Email,
        public readonly fullName: FullName,
        public readonly photo: PhotoPath | null
    ) {
    }
}

export default PersonalUserInfo;