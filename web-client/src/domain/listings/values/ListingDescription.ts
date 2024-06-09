import ValidationException from "../../auth/interfaces/services/ValidationException";
import Assert from "../../utils/Assert";

class ListingDescription {
    public static readonly MAX_LENGTH = 500;
    public static readonly MIN_LENGTH = 1;

    private constructor(public readonly value: string) {
    }

    private static validate(value: string): void {
        Assert.notBlank(value, () => new ListingDescription.MinLengthException());
        Assert.minLength(
            value,
            ListingDescription.MIN_LENGTH,
            () => new ListingDescription.MinLengthException()
        );
        Assert.maxLength(
            value,
            ListingDescription.MAX_LENGTH,
            () => new ListingDescription.MaxLengthException()
        );
    }

    public static checkAndCreate = (value: string): ListingDescription => {
        ListingDescription.validate(value);
        return new ListingDescription(value);
    }

    public static dangerouslyCreate = (value: string): ListingDescription => {
        return new ListingDescription(value);
    }

    static readonly ValidationException = ValidationException;

    static readonly MinLengthException = class extends ListingDescription.ValidationException {
        public constructor() {
            super(
                `First name must be at least ${ListingDescription.MIN_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MinLength";
        };
    };

    static readonly MaxLengthException = class extends ListingDescription.ValidationException {
        public constructor() {
            super(
                `First name must be at most ${ListingDescription.MAX_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MaxLength";
        };
    };
}

export default ListingDescription;
