class Handler {

    static get type() {
        return this.name;
    }

    get type() {
        return this.constructor.name;
    }

    get isHandler() {
        return true;
    }
}

export default Handler;