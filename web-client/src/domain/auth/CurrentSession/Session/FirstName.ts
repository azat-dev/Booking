import Assert from "../../../utils/Assert";
import ValidationException from "./ValidationException";

class FirstName {
    public static readonly MAX_LENGTH = 50;
    public static readonly MIN_LENGTH = 1;

    public readonly value: string;

    public constructor(value: string) {
        const cleanedValue = value.trim();

        Assert.notBlank(value, () => new FirstName.MinLengthException());
        Assert.minLength(
            cleanedValue,
            FirstName.MIN_LENGTH,
            () => new FirstName.MinLengthException()
        );
        Assert.maxLength(
            cleanedValue,
            FirstName.MAX_LENGTH,
            () => new FirstName.MaxLengthException()
        );

        this.value = cleanedValue;
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
