import FirstName from "./FirstName";
import LastName from "./LastName";

class FullName {
    public constructor(
        public readonly firstName: FirstName,
        public readonly lastName: LastName
    ) {
    }

    public toString(): string {
        return `${this.firstName.value} ${this.lastName.value}`;
    }

    public getInitials = (): string => {
        return `${this.firstName.value[0]}${this.lastName.value[0]}`;
    }
}

export default FullName;
