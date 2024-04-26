import AdultQuantity from "./AdultQuantity";
import ChildrenQuantity from "./ChildrenQuantity";

export default class GuestsQuantity {
    public constructor(
        public readonly adults: AdultQuantity,
        public readonly children: ChildrenQuantity = ChildrenQuantity.NoChildren
    ) {}
}
