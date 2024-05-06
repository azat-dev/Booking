import Assert from "../../utils/Assert";
import ValidationException from "../CurrentSession/Session/ValidationException";

class Email {
    public readonly value: string;

    constructor(value: string) {
        const cleanedValue = value.trim();

        Assert.notBlank(cleanedValue, () => new Email.NotBlank());
        Assert.hasPattern(
            cleanedValue,
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            () => new Email.FormatException()
        );

        this.value = cleanedValue;
    }

    public static readonly ValidationException = ValidationException;

    public static NotBlank = class extends Email.ValidationException {
        public constructor() {
            super("Email is required");
        }

        public getCode = (): string => {
            return "NotBlank";
        };
    };

    public static FormatException = class extends Email.ValidationException {
        public constructor() {
            super("Invalid email format");
        }

        public getCode = (): string => {
            return "InvalidFormat";
        };
    };
}

export default Email;
