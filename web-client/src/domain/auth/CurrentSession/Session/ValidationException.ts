class ValidationException extends Error {
    public constructor(message: string) {
        super(message);
    }

    public getCode = (): string => {
        throw new Error("Method not implemented.");
    };
}

export default ValidationException;
