import AppEvent from "../../utils/AppEvent";

class UserLoggedOut extends AppEvent {

    public static readonly TYPE = "USER_LOGGED_OUT";
}

export default UserLoggedOut;