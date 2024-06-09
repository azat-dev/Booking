import ValidationException from "../../auth/interfaces/services/ValidationException";
import Assert from "../../utils/Assert";

class ListingTitle {
    public static readonly MAX_LENGTH = 40;
    public static readonly MIN_LENGTH = 1;

    private constructor(public readonly value: string) {
    }

    private static validate(value: string): void {
        Assert.notBlank(value, () => new ListingTitle.MinLengthException());
        Assert.minLength(
            value,
            ListingTitle.MIN_LENGTH,
            () => new ListingTitle.MinLengthException()
        );
        Assert.maxLength(
            value,
            ListingTitle.MAX_LENGTH,
            () => new ListingTitle.MaxLengthException()
        );
    }

    public static checkAndCreate = (value: string): ListingTitle => {
        ListingTitle.validate(value);
        return new ListingTitle(value);
    }

    public static dangerouslyCreate = (value: string): ListingTitle => {
        return new ListingTitle(value);
    }

    static readonly ValidationException = ValidationException;

    static readonly MinLengthException = class extends ListingTitle.ValidationException {
        public constructor() {
            super(
                `First name must be at least ${ListingTitle.MIN_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MinLength";
        };
    };

    static readonly MaxLengthException = class extends ListingTitle.ValidationException {
        public constructor() {
            super(
                `First name must be at most ${ListingTitle.MAX_LENGTH} characters long`
            );
        }

        getCode = (): string => {
            return "MaxLength";
        };
    };
}

export default ListingTitle;
