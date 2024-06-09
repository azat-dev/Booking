import KeepType from "./KeepType.ts";
import VM from "../../presentation/utils/VM.ts";


abstract class Command extends KeepType {

    public readonly id: string;
    public senderId: string | undefined;

    public constructor(id: string | undefined = undefined) {
        super();
        this.id = id ?? crypto.randomUUID();
    }

    get isCommand() {
        return true;
    }

    public setSenderId(senderId: string) {
        this.senderId = senderId;
    }

    public withSender = (sender: VM | string | undefined) => {

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

export default Command;