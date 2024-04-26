import GuestsQuantity from "../../../../domain/booking/values/GuestsQuantity";
import value from "../../../utils/binding/value";
import ItemViewModel from "./item/ItemViewModel";

class GuestsQuantityListViewModel {
    public readonly adultsItem: ItemViewModel;
    public readonly childrenItem: ItemViewModel;

    public constructor(
        initialQuantity: GuestsQuantity,
        onChange: (guests: GuestsQuantity) => void
    ) {
        this.adultsItem = new ItemViewModel(
            initialQuantity.adults.val,
            () => {},
            () => {}
        );

        this.childrenItem = new ItemViewModel(
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

export default GuestsQuantityListViewModel;
