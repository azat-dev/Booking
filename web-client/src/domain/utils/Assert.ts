export interface ExceptionProvider {
    (): Error;
}

export default class Assert {
    public static notNull<T, E extends Error>(
        value: T,
        exceptionSupplier: ExceptionProvider
    ): void {
        if (value === null) {
            throw exceptionSupplier();
        }
    }

    public static notBlank(
        value: string,
        exceptionSupplier: ExceptionProvider
    ): void {
        if (value == null || value.trim().length === 0) {
            throw exceptionSupplier();
        }
    }

    public static hasPattern(
        value: string,
        pattern: RegExp,
        exceptionSupplier: ExceptionProvider
    ): void {
        if (!pattern.test(value)) {
            throw exceptionSupplier();
        }
    }

    public static isTrue(
        value: boolean,
        exceptionSupplier: ExceptionProvider
    ): void {
        if (!value) {
            throw exceptionSupplier();
        }
    }

    public static isFalse(
        value: boolean,
        exceptionSupplier: ExceptionProvider
    ): void {
        if (value) {
            throw exceptionSupplier();
        }
    }

    public static minLength(
        value: string | Array<any>,
        minLength: number,
        exceptionSupplier: ExceptionProvider
    ): void {
        Assert.isTrue(value.length >= minLength, exceptionSupplier);
    }

    public static maxLength(
        value: string | Array<any>,
        maxLength: number,
        exceptionSupplier: ExceptionProvider
    ): void {
        Assert.isTrue(value.length < maxLength, exceptionSupplier);
    }
}
