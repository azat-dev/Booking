import GuestsQuantity from "../../../../domain/booking/values/GuestsQuantity";
import value from "../../../utils/binding/value";
import ItemVM from "./item/ItemVM";

class GuestsQuantityListVM {
    public readonly adultsItem: ItemVM;
    public readonly childrenItem: ItemVM;

    public constructor(
        initialQuantity: GuestsQuantity,
        onChange: (guests: GuestsQuantity) => void
    ) {
        this.adultsItem = new ItemVM(
            initialQuantity.adults.val,
            () => {},
            () => {}
        );

        this.childrenItem = new ItemVM(
            initialQuantity.children.val,
            () => {},
            () => {}
        );
    }

    public updateQuantity = (guests: GuestsQuantity): void => {
        this.adultsItem.updateValue(guests.adults.val);
        this.childrenItem.updateValue(guests.children.val);
    };
}

export default GuestsQuantityListVM;
