import dayjs, { Dayjs } from "dayjs";

export default class Date {
    private value: Dayjs;

    private constructor(value: Dayjs) {
        this.value = dayjs(value.startOf("day").toISOString());
    }

    public static fromString(value: string) {
        return new Date(dayjs(value));
    }

    public static fromDate(value: globalThis.Date) {
        return new Date(dayjs(value));
    }

    public isAfter = (date: Date): boolean => {
        return this.value.isAfter(date.value);
    };

    public isBefore = (date: Date): boolean => {
        return this.value.isBefore(date.value);
    };

    public isSame = (date: Date): boolean => {
        return this.value.isSame(date.value);
    };
}
