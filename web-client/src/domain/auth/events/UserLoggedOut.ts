import AppEvent from "../../utils/AppEvent";

class UserLoggedOut extends AppEvent {

    public static readonly TYPE = "UserLoggedOut";
}

export default UserLoggedOut;