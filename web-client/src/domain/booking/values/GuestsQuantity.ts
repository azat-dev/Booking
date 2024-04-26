import AdultQuantity from "./AdultQuantity";
import ChildrenQuantity from "./ChildrenQuantity";

export class AtLeastOneAdultRequiredError extends Error {
    public constructor() {
        super("At least one adult is required");
    }
}

export default class GuestsQuantity {
    public constructor(
        public readonly adults: AdultQuantity,
        public readonly children: ChildrenQuantity = ChildrenQuantity.NoChildren
    ) {
        if (adults.val < 1) {
            throw new AtLeastOneAdultRequiredError();
        }
    }

    public toString = (): string => {
        let result = `${this.adults.val}`;
        if (this.adults.val > 1) {
            result += " adults";
        } else {
            result += " adult";
        }

        if (this.children.val > 0) {
            result += `, ${this.children.val}`;

            if (this.children.val > 1) {
                result += " children";
            } else {
                result += " child";
            }
        }

        return result;
    };
}
