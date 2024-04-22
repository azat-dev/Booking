class Email {
    private value: string;

    constructor(value: string) {
        if (!Email.isValid(value)) {
            throw new Error("Invalid email");
        }

        this.value = value;
    }

    private static isValid(email: string): boolean {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    public getValue(): string {
        return this.value;
    }
}

export default Email;
