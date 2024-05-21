abstract class AppEvent {
    static readonly TYPE: string = 'AppEvent';

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isEvent() {
        return true;
    }
}

export default AppEvent;