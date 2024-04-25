import RangePickerCalendarViewModel from "../range-picker-calendar/RangePickerCalendarViewModel";
import { AvailableDates, CalendarRange } from "../month-view/MonthViewModel";
import Subject from "../../../utils/binding/Subject";
import dayjs from "dayjs";
import value from "../../../utils/binding/value";

class DateRangePickerViewModel {
    public readonly calendar: RangePickerCalendarViewModel;
    public readonly checkIn: Subject<string | undefined>;
    public readonly checkOut: Subject<string | undefined>;

    public constructor(
        initialRange: CalendarRange | undefined,
        availableDates: AvailableDates,
        onChange: (range: CalendarRange) => void
    ) {
        this.calendar = new RangePickerCalendarViewModel(
            undefined,
            availableDates,
            onChange
        );
        this.checkIn = value(this.formatDate(initialRange?.start));
        this.checkOut = value(this.formatDate(initialRange?.end));
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
}

export default DateRangePickerViewModel;
