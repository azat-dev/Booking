import Subject from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";
import ButtonVM from "./button/ButtonVM";

class ItemVM {
    public readonly incrementButton: ButtonVM;
    public readonly decrementButton: ButtonVM;

    public readonly value: Subject<number>;

    public constructor(
        public readonly initialValue: number,
        private readonly onDecrement: () => void,
        private readonly onIncrement: () => void
    ) {
        this.value = value(initialValue);
        this.incrementButton = new ButtonVM(false, this.onIncrement);
        this.decrementButton = new ButtonVM(false, this.onDecrement);
    }

    public updateValue = (value: number): void => {
        this.value.set(value);
    };
}

export default ItemVM;
