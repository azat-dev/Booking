import KeepType from "./KeepType.ts";

class Command extends KeepType {

    get isCommand() {
        return true;
    }

    execute() {
        console.log('Command executed');
    }
}

export default Command;