export default class Price {
    public constructor(
        public readonly price: number,
        public readonly currency: string
    ) {}

    public toString = (): string => {
        return `${this.currency}${this.price}`;
    };
}
