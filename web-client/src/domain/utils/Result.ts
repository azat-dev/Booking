export default class Result<T, E extends Error> {
    public static success<T>(data: T): Result<T, never> {
        return new Result(data, null) as any;
    }

    public static failure<E extends Error>(error: E): Result<never, E> {
        return new Result(null, error) as any;
    }

    private constructor(
        public readonly data: T | null,
        public readonly error: E | null
    ) {}

    public isSuccess(): boolean {
        return this.data !== null;
    }

    public isFailure(): boolean {
        return this.error !== null;
    }

    public getOrElseThrow(): T {
        if (this.data === null) {
            throw this.error;
        }

        return this.data;
    }
}
