import Assert from "../../utils/Assert";
import ValidationException from "../interfaces/services/ValidationException";

class Email {

    public static readonly PATTERN = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    private constructor(public readonly value: string) {
    }

    private static validate = (value: string): void  => {
        Assert.notBlank(value, () => new Email.NotBlank());
        Assert.hasPattern(
            value,
            Email.PATTERN,
            () => new Email.FormatException()
        );
    }

    public static checkAndCreateFromString(value: string): Email {
        const cleanedValue = value.trim().toLowerCase();
        Email.validate(cleanedValue);
        return new Email(value);
    }

    public static dangerouslyCreate(value: string): Email {
        return new Email(value);
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
