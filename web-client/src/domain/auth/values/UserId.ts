export default class UserId {
    private constructor(public readonly val: string) {}

    public static fromString(val: string): UserId {
        return new UserId(val);
    }
}
