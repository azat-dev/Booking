import TYPE_INFO from "../../generated/EVENTS_TYPE_INFO";

abstract class AppEvent {
    static readonly TYPE: string = 'AppEvent';

    static get type() {
        return TYPE_INFO[this.name];
    }

    get type() {
        return TYPE_INFO[this.constructor.name];
    }

    get isEvent() {
        return true;
    }
}

export default AppEvent;