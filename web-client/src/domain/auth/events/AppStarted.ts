import AppEvent from "../../utils/AppEvent";

class AppStarted extends AppEvent {

    public constructor() {
        super('none');
    }
}

export default AppStarted;