import UserId from "./UserId";
import Email from "./Email";
import FullName from "./FullName";
import {PhotoPath} from "./PhotoPath";

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