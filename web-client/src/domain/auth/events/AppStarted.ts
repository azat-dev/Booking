import AppEvent from "../../utils/AppEvent";


class AppStarted extends AppEvent {

    public static TYPE = 'APP_STARTED';
}

export default AppStarted;