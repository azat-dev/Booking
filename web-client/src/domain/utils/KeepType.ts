abstract class KeepType {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }
}

export default KeepType;