import GuestsQuantity from "../../../domain/booking/values/GuestsQuantity";
import Subject from "../../utils/binding/Subject";
import value from "../../utils/binding/value";
import GuestsQuantityListViewModel from "./guests-quantity-list/GuestsQuantityListViewModel";

class GuestsQuantityInputViewModel {
    public readonly guestsQuantityList: GuestsQuantityListViewModel;
    public readonly guestsSummary: Subject<string>;
    public constructor(
        initialQuantity: GuestsQuantity,
        onChange: (guests: GuestsQuantity) => void
    ) {
        this.guestsQuantityList = new GuestsQuantityListViewModel(
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

export default GuestsQuantityInputViewModel;
