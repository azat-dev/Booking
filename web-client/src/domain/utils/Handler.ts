class Handler {

    public static readonly TYPE: string = 'Handler';

    get type() {
        return (this.constructor as any).TYPE;
    }

    get isHandler() {
        return true;
    }
}

export default Handler;