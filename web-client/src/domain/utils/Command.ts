import TYPE_INFO from "../../generated/COMMANDS_TYPE_INFO";

class Command {

    static get type() {
        return TYPE_INFO[this.name];
    }

    get type() {
        return TYPE_INFO[this.constructor.name];
    }
    get isCommand() {
        return true;
    }

    execute() {
        console.log('Command executed');
    }
}

export default Command;