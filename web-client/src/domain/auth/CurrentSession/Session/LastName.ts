import Assert from "../../../utils/Assert";
import ValidationException from "./ValidationException";

class LastName {
    public static readonly MAX_LENGTH = 50;
    public static readonly MIN_LENGTH = 1;

    public readonly value: string;

    public constructor(value: string) {
        const cleanedValue = value.trim();

        Assert.notBlank(cleanedValue, () => new LastName.MinLengthException());
        Assert.minLength(
            cleanedValue,
            LastName.MIN_LENGTH,
            () => new LastName.MinLengthException()
        );
        Assert.maxLength(
            cleanedValue,
            LastName.MAX_LENGTH,
            () => new LastName.MaxLengthException()
        );

        this.value = cleanedValue;
    }

    static readonly ValidationException = ValidationException;

    static readonly MinLengthException = class extends LastName.ValidationException {
        public constructor() {
            super(
                `Last name must be at least ${LastName.MIN_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MinLength";
        };
    };

    static readonly MaxLengthException = class extends LastName.ValidationException {
        public constructor() {
            super(
                `Last name must be at most ${LastName.MAX_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MaxLength";
        };
    };
}

export default LastName;
