import Price from "./Price";

export default class Cost {
    public constructor(
        public readonly totalCost: Price,
        public readonly accommodationCost: Price,
        public readonly serviceFee: Price
    ) {}
}
