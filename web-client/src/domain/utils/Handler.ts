import TYPE_INFO from "../../generated/HANDLERS_TYPE_INFO";

class Handler {

    static get type() {
        return TYPE_INFO[this.name];
    }

    get type() {
        return TYPE_INFO[this.constructor.name];
    }

    get isHandler() {
        return true;
    }
}

export default Handler;