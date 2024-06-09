import VM from "../../presentation/utils/VM.ts";

abstract class AppEvent {

    static readonly TYPE: string = 'AppEvent';

    public readonly id: string;
    public senderId: string | undefined;

    public constructor(id: string | undefined = undefined) {
        this.id = id ?? crypto.randomUUID();
    }

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isEvent() {
        return true;
    }

    public setSenderId = (senderId: string | undefined) => {
        this.senderId = senderId;
    }

    public withSender = (sender: string | undefined | VM) => {
        if (!sender) {
            return this;
        }

        if (typeof sender === 'string') {
            this.setSenderId(sender);
            return this;
        }

        this.setSenderId(sender.vmId);
        return this;
    }
}

export default AppEvent;