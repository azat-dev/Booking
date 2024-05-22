import KeepType from "./KeepType.ts";

class Policy extends KeepType {

    get isPolicy() {
        return true;
    }
}

export default Policy;