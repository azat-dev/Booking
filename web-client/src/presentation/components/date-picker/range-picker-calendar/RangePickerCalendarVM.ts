import dayjs from "dayjs";
import MonthVM, {
    AvailableDates,
    CalendarRange,
    MonthPosition,
} from "../month-view/MonthVM";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";

class RangePickerCalendarVM {
    private currentPosition: MonthPosition;
    public leftMonth: Subject<MonthVM>;
    public rightMonth: Subject<MonthVM>;

    public leftMonthTitle: Subject<string>;
    public rightMonthTitle: Subject<string>;

    private selectionRange: CalendarRange | undefined;

    public constructor(
        initialOpenedPosition: MonthPosition | undefined,
        private availableDates: AvailableDates,
        private onChange: (range: CalendarRange) => void
    ) {
        this.currentPosition =
            initialOpenedPosition ||
            new MonthPosition(dayjs().month(), dayjs().year());

        const nextMonthPosition = this.currentPosition.nextMonth();

        this.leftMonth = value(this.getMonthVm(this.currentPosition));
        this.leftMonthTitle = value(this.getMonthTitle(this.currentPosition));

        this.rightMonth = value(
            this.getMonthVm(this.currentPosition.nextMonth())
        );

        this.rightMonthTitle = value(this.getMonthTitle(nextMonthPosition));
    }

    private getMonthTitle = (position: MonthPosition) => {
        const months = [
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
        ];
        return `${months[position.month]} ${position.year}`;
    };

    private getMonthVm = (position: MonthPosition) => {
        return new MonthVM(
            position,
            this.selectionRange,
            this.clickDay,
            this.availableDates
        );
    };

    private showRange = () => {
        this.leftMonth.value.updateSelection(this.selectionRange);
        this.rightMonth.value.updateSelection(this.selectionRange);
    };

    private clickDay = (day: Date) => {
        if (!this.selectionRange) {
            const newRange = new CalendarRange(day, undefined);
            this.onChange(newRange);
            return;
        }

        if (!this.selectionRange.end) {
            const start = this.selectionRange.start;

            const newRange = new CalendarRange(start, day);
            this.onChange(newRange);
            return;
        }

        const newRange = new CalendarRange(day, undefined);
        this.onChange(newRange);
    };

    public showPrev = () => {
        const prevMonth = this.currentPosition.prevMonth();
        if (!prevMonth) {
            return;
        }

        this.currentPosition = prevMonth;
        const nextMonth = this.currentPosition.nextMonth();

        this.leftMonth.set(this.getMonthVm(this.currentPosition));
        this.rightMonth.set(this.getMonthVm(nextMonth));

        this.leftMonthTitle.set(this.getMonthTitle(this.currentPosition));
        this.rightMonthTitle.set(this.getMonthTitle(nextMonth));
    };

    public showNext = () => {
        this.currentPosition = this.currentPosition.nextMonth();
        const nextMonth = this.currentPosition.nextMonth();

        this.leftMonth.set(this.getMonthVm(this.currentPosition));
        this.rightMonth.set(this.getMonthVm(this.currentPosition.nextMonth()));

        this.leftMonthTitle.set(this.getMonthTitle(this.currentPosition));
        this.rightMonthTitle.set(this.getMonthTitle(nextMonth));
    };

    public updateRange = (range: CalendarRange) => {
        this.selectionRange = range;
        this.showRange();
    };
}

export default RangePickerCalendarVM;
