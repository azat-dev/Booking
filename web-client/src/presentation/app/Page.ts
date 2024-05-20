abstract class Page {
    public static readonly TYPE: string = "PAGE";

    public abstract get type(): string;
}

export default Page;