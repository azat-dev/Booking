class Command {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isCommand() {
        return true;
    }

    execute() {
        console.log('Command executed');
    }
}

export default Command;