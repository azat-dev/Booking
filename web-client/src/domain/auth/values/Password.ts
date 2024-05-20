import Assert from "../../utils/Assert";
import ValidationException from "../interfaces/services/ValidationException";

export default class Password {
    public static MIN_LENGTH = 6;

    public static readonly NO_WHITESPACES_PATTERN = /\S+/;

    public static readonly ONLY_ALPHABETS_DIGITS_AND_SPECIAL_CHARACTERS_PATTERN =
        /^[a-zA-Z0-9!@#$%^&*()_+{}|:<>?~]+$/;

    public readonly value: string;

    public constructor(value: string) {
        Assert.minLength(
            value,
            Password.MIN_LENGTH,
            () => new Password.LengthException()
        );

        Assert.hasPattern(
            value,
            Password.NO_WHITESPACES_PATTERN,
            () => new Password.WhitespacesNotAllowedException()
        );

        Assert.hasPattern(
            value,
            Password.ONLY_ALPHABETS_DIGITS_AND_SPECIAL_CHARACTERS_PATTERN,
            () => new Password.OnlyAlphabetsDigitsAndSpecialCharactesException()
        );

        this.value = value;
    }

    public static readonly ValidationException = ValidationException;

    public static readonly LengthException = class extends Password.ValidationException {
        public constructor() {
            super(
                `Password must be at least ${Password.MIN_LENGTH} characters long`
            );
        }

        public getCode = (): string => {
            return "WrongLength";
        };
    };

    public static readonly WhitespacesNotAllowedException = class extends Password.ValidationException {
        public constructor() {
            super("Password can't contain whitespaces");
        }

        public getCode = (): string => {
            return "WhitespacesNotAllowed";
        };
    };

    public static readonly OnlyAlphabetsDigitsAndSpecialCharactesException = class extends Password.ValidationException {
        public constructor() {
            super(
                "Only alphabets, digits \nand special characters: !@#$%^&*()_+{}|:<>?~"
            );
        }

        public getCode = (): string => {
            return "OnlyAlphabetsDigitsAndSpecialCharactes";
        };
    };
}
