import FirstName from "./FirstName";
import LastName from "./LastName";

class FullName {
    public constructor(
        public readonly firstName: FirstName,
        public readonly lastName: LastName
    ) {}

    public toString(): string {
        return `${this.firstName.value} ${this.lastName.value}`;
    }
}

export default FullName;
