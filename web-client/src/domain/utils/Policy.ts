class Policy {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isPolicy() {
        return true;
    }
}

export default Policy;