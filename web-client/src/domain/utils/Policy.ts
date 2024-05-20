class Policy {

    public static readonly TYPE: string = 'Policy';

    get type() {
        return (this.constructor as any).TYPE;
    }

    get isPolicy() {
        return true;
    }
}

export default Policy;