class Command {

    public static readonly TYPE: string = 'Command';

    get type() {
        return (this.constructor as any).TYPE;
    }

    get isCommand() {
        return true;
    }

    execute() {
        console.log('Command executed');
    }
}

export default Command;