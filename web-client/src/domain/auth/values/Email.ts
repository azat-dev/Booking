import Assert from "../../utils/Assert";
import ValidationException from "../CurrentSession/Session/ValidationException";

class Email {
    public readonly value: string;

    public static readonly PATTERN = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    constructor(value: string) {
        const cleanedValue = value.trim();

        Assert.notBlank(cleanedValue, () => new Email.NotBlank());
        Assert.hasPattern(
            cleanedValue,
            Email.PATTERN,
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

    public toString = (): string => {
        return this.value;
    };
}

export default Email;
