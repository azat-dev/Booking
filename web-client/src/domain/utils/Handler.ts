import KeepType from "./KeepType.ts";
import Command from "./Command.ts";

abstract class Handler extends KeepType {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isHandler() {
        return true;
    }

    abstract execute(command: Command): Promise<void>;
}

export default Handler;