abstract class AppEvent {
    static readonly TYPE: string = 'AppEvent';

    get type() {
        return (this.constructor as any).TYPE;
    }

    get isEvent() {
        return true;
    }
}

export default AppEvent;