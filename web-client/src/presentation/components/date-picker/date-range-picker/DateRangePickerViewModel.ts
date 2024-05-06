import RangePickerCalendarViewModel from "../range-picker-calendar/RangePickerCalendarViewModel";
import {
    AvailableDates,
    CalendarRange,
    MonthPosition,
} from "../month-view/MonthViewModel";
import Subject from "../../../utils/binding/Subject";
import dayjs from "dayjs";
import value from "../../../utils/binding/value";

class DateRangePickerViewModel {
    public readonly calendar: RangePickerCalendarViewModel;
    public readonly isOpened: Subject<boolean>;
    public readonly isDisabled: Subject<boolean>;
    public readonly checkIn: Subject<string | undefined>;
    public readonly checkOut: Subject<string | undefined>;

    public constructor(
        initialRange: CalendarRange | undefined,
        availableDates: AvailableDates,
        onChange: (range: CalendarRange) => void,
        initialIsDisabled: boolean = false
    ) {
        this.calendar = new RangePickerCalendarViewModel(
            initialRange
                ? new MonthPosition(
                      initialRange.start.getMonth(),
                      initialRange.start.getFullYear()
                  )
                : undefined,
            availableDates,
            onChange
        );
        this.checkIn = value(this.formatDate(initialRange?.start));
        this.checkOut = value(this.formatDate(initialRange?.end));
        this.isDisabled = value(initialIsDisabled);
        this.isOpened = value(false);
    }

    private formatDate = (date: Date | undefined) => {
        if (!date) {
            return undefined;
        }

        return dayjs(date).format("MMM D, YYYY");
    };

    public updateRange = (range: CalendarRange): void => {
        this.calendar.updateRange(range);
        this.checkIn.set(this.formatDate(range.start));
        this.checkOut.set(this.formatDate(range.end));
    };

    public open = () => {
        this.isOpened.set(true);
    };

    public close = () => {
        this.isOpened.set(false);
    };

    public updateIsDisabled = (isDisabled: boolean) => {
        this.isDisabled.set(isDisabled);
    };

    public toggleOpen = () => {
        this.isOpened.set(!this.isOpened.value);
    };

    public clickAway = () => {
        this.close();
    };
}

export default DateRangePickerViewModel;
