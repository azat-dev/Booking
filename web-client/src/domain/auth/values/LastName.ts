import Assert from "../../utils/Assert";
import ValidationException from "../interfaces/services/ValidationException";

class LastName {
    public static readonly MAX_LENGTH = 50;
    public static readonly MIN_LENGTH = 1;

    private constructor(public readonly value: string) {
    }

    private static validate =(value: string): void  =>{

        Assert.notBlank(value, () => new LastName.MinLengthException());
        Assert.minLength(
            value,
            LastName.MIN_LENGTH,
            () => new LastName.MinLengthException()
        );
        Assert.maxLength(
            value,
            LastName.MAX_LENGTH,
            () => new LastName.MaxLengthException()
        );
    }

    static dangerouslyCreate = (value: string): LastName => {
        return new LastName(value);
    }

    static checkAndCreate = (value: string): LastName => {
        LastName.validate(value);
        return new LastName(value);
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
