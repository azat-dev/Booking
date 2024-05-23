import KeepType from "./KeepType.ts";
import AppEvent from "./AppEvent.ts";

abstract class Policy extends KeepType {

    get isPolicy() {
        return true;
    }

    abstract execute(event: AppEvent): void;
}

export default Policy;