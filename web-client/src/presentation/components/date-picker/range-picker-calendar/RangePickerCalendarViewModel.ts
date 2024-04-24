import dayjs from "dayjs";
import MonthViewModel, {
    CalendarRange,
    MonthPosition,
} from "../month-view/MonthViewModel";

class RangePickerCalendarViewModel {
    public readonly leftMonth: MonthViewModel;
    public readonly rightMonth: MonthViewModel;

    private selectionRange: CalendarRange | undefined;

    public constructor() {
        const leftMonthPosition = new MonthPosition(4, 2024);
        const rightMonthPosition = new MonthPosition(5, 2024);

        this.leftMonth = new MonthViewModel(
            leftMonthPosition,
            this.selectionRange,
            this.clickDay,
            this.isDisabledDate
        );
        this.rightMonth = new MonthViewModel(
            rightMonthPosition,
            this.selectionRange,
            this.clickDay,
            this.isDisabledDate
        );
    }

    private isDisabledDate = (date: Date) => {
        return false;
    };

    private updateRange = () => {
        this.leftMonth.updateSelection(this.selectionRange);
        this.rightMonth.updateSelection(this.selectionRange);
    };

    private clickDay = (day: Date) => {
        if (!this.selectionRange) {
            this.selectionRange = new CalendarRange(day, undefined);
        } else if (!this.selectionRange.end) {
            const start = this.selectionRange.start;

            if (dayjs(start).isBefore(day)) {
                this.selectionRange = new CalendarRange(start, day);
            } else {
                this.selectionRange = new CalendarRange(day, start);
            }
        } else {
            this.selectionRange = new CalendarRange(day, undefined);
        }

        this.updateRange();
    };
}

export default RangePickerCalendarViewModel;
