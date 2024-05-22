import KeepType from "./KeepType.ts";

class Handler extends KeepType {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isHandler() {
        return true;
    }
}

export default Handler;