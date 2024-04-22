export default class Password {
    public static MIN_LENGTH = 6;

    public constructor(public readonly value: string) {
        if (!Password.isValid(value)) {
            throw new Error("Invalid password");
        }
    }

    private static isValid(password: string): boolean {
        return password.length >= this.MIN_LENGTH;
    }
}
