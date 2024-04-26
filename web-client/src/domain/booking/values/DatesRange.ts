import Date from "./Date";

export default class DatesRange {
    public constructor(
        public readonly startDate: Date,
        public readonly endDate: Date
    ) {}

    public getNightCount = (): number => {
        return this.startDate.diff(this.endDate);
    };
}
