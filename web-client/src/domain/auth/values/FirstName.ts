import Assert from "../../utils/Assert";
import ValidationException from "../interfaces/services/ValidationException";

class FirstName {
    public static readonly MAX_LENGTH = 50;
    public static readonly MIN_LENGTH = 1;

    private constructor(public readonly value: string) {
    }

    private static validate(value: string): void {
        Assert.notBlank(value, () => new FirstName.MinLengthException());
        Assert.minLength(
            value,
            FirstName.MIN_LENGTH,
            () => new FirstName.MinLengthException()
        );
        Assert.maxLength(
            value,
            FirstName.MAX_LENGTH,
            () => new FirstName.MaxLengthException()
        );
    }

    public static checkAndCreate = (value: string): FirstName => {
        FirstName.validate(value);
        return new FirstName(value);
    }

    public static dangerouslyCreate = (value: string): FirstName => {
        return new FirstName(value);
    }

    static readonly ValidationException = ValidationException;

    static readonly MinLengthException = class extends FirstName.ValidationException {
        public constructor() {
            super(
                `First name must be at least ${FirstName.MIN_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MinLength";
        };
    };

    static readonly MaxLengthException = class extends FirstName.ValidationException {
        public constructor() {
            super(
                `First name must be at most ${FirstName.MAX_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MaxLength";
        };
    };
}

export default FirstName;
