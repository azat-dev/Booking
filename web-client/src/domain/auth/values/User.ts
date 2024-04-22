import Avatar from "./Avatar";
import Email from "./Email";
import UserId from "./UserId";

export default interface User {
    readonly id: UserId;
    readonly email: Email;
    readonly name: string | null;
    readonly avatar: Avatar | null;
}
