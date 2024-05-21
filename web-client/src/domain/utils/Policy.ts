import POLICIES_TYPE_INFO from "../../generated/POLICIES_TYPE_INFO";

class Policy {

    static get type() {
        return POLICIES_TYPE_INFO[this.name];
    }

    get type() {
        return POLICIES_TYPE_INFO[this.constructor.name];
    }

    get isPolicy() {
        return true;
    }
}

export default Policy;