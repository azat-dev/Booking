import FirstName from "./FirstName";
import LastName from "./LastName";

class FullName {
    public constructor(
        public readonly firstName: FirstName,
        public readonly lastName: LastName
    ) {}
}

export default FullName;
