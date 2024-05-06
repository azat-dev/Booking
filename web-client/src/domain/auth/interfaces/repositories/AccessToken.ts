import Assert from "../../../utils/Assert";
import ValidationException from "../../CurrentSession/Session/ValidationException";

export default class AccessToken {
    public constructor(public readonly val: string) {
        Assert.notBlank(val, () => new AccessToken.InvalidFormatException());
    }

    public static readonly ValidationException = ValidationException;

    public static InvalidFormatException = class extends AccessToken.ValidationException {
        public constructor() {
            super("Invalid access token format");
        }

        public getCode = (): string => {
            return "InvalidFormat";
        };
    };
}
