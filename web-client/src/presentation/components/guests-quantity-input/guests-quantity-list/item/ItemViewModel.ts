import Subject from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";
import ButtonViewModel from "./button/ButtonViewModel";

class ItemViewModel {
    public readonly incrementButton: ButtonViewModel;
    public readonly decrementButton: ButtonViewModel;

    public readonly value: Subject<number>;

    public constructor(
        public readonly initialValue: number,
        private readonly onDecrement: () => void,
        private readonly onIncrement: () => void
    ) {
        this.value = value(initialValue);
        this.incrementButton = new ButtonViewModel(false, this.onIncrement);
        this.decrementButton = new ButtonViewModel(false, this.onDecrement);
    }

    public updateValue = (value: number): void => {
        this.value.set(value);
    };
}

export default ItemViewModel;
