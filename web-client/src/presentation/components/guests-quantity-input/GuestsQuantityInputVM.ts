import GuestsQuantity from "../../../domain/booking/values/GuestsQuantity";
import Subject from "../../utils/binding/Subject";
import value from "../../utils/binding/value";
import GuestsQuantityListVM from "./guests-quantity-list/GuestsQuantityListVM";

class GuestsQuantityInputVM {
    public readonly guestsQuantityList: GuestsQuantityListVM;
    public readonly guestsSummary: Subject<string>;
    public constructor(
        initialQuantity: GuestsQuantity,
        onChange: (guests: GuestsQuantity) => void
    ) {
        this.guestsQuantityList = new GuestsQuantityListVM(
            initialQuantity,
            onChange
        );

        this.guestsSummary = value(initialQuantity.toString());
    }

    public updateQuantity = (guests: GuestsQuantity): void => {
        this.guestsQuantityList.updateQuantity(guests);
        this.guestsSummary.set(guests.toString());
    };
}

export default GuestsQuantityInputVM;
