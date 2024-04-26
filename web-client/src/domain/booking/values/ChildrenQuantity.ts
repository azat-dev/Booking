export default class ChildrenQuantity {
    public constructor(public readonly val: number) {}

    public static readonly NoChildren = new ChildrenQuantity(0);
}
