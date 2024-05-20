import FullName from "./FullName";
import Avatar from "./Avatar";
import Email from "./Email";
import UserId from "./UserId";

export default interface UserInfo {
    readonly id: UserId;
    readonly email: Email;
    readonly fullName: FullName;
    readonly avatar: Avatar | null;
}
