abstract class KeepType {

    static get type() {
        return this.name;
    }

    get type() {
        return (this.constructor as any).type;
    }
}

export default KeepType;