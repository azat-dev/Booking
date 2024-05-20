import DayCellVM, { SelectionState } from "./day-cell/DayCellVM";
import dayjs from "dayjs";
import WeekDayCellVM from "./week-day-cell/WeekDayCellVM";

export enum CellType {
    Day,
    Empty,
}

export type CellVM =
    | {
          type: CellType.Empty;
      }
    | {
          type: CellType.Day;
          vm: DayCellVM;
      };

export class MonthPosition {
    public constructor(
        public readonly month: number,
        public readonly year: number
    ) {}

    public nextMonth = () => {
        const nextMonth = this.month + 1 >= 12 ? 0 : this.month + 1;
        const nextYear = this.month + 1 >= 12 ? this.year + 1 : this.year;
        return new MonthPosition(nextMonth, nextYear);
    };

    public prevMonth = () => {
        if (this.year === 0 && this.month === 0) {
            return undefined;
        }

        const prevMonth = this.month - 1 < 0 ? 11 : this.month - 1;
        const prevYear = this.month - 1 < 0 ? this.year - 1 : this.year;

        return new MonthPosition(prevMonth, prevYear);
    };
}

export class CalendarRange {
    public start: Date;
    public end: Date | undefined;

    private static toStartOfDay = (date: Date): Date =>
        dayjs(date).startOf("day").toDate();

    public constructor(start: Date, end: Date | undefined) {
        this.start = CalendarRange.toStartOfDay(start);
        this.end = end && CalendarRange.toStartOfDay(end);

        if (this.end) {
            if (dayjs(this.end).isBefore(this.start)) {
                const tmp = this.start;
                this.start = this.end;
                this.end = tmp;
            }
        }
    }

    public contains = (date: Date): boolean => {
        if (!this.end) {
            return this.isStart(date);
        }

        if (dayjs(date).isBefore(this.start, "day")) {
            return false;
        }

        if (dayjs(date).isAfter(this.end, "day")) {
            return false;
        }

        return true;
    };

    public isStart = (date: Date): boolean => {
        return dayjs(date).isSame(this.start, "day");
    };

    public isEnd = (date: Date): boolean => {
        if (!this.end) {
            return false;
        }
        return dayjs(date).isSame(this.end, "day");
    };
}

export abstract class AvailableDates {
    abstract isAvailable(date: Date): boolean;
}

class MonthVM {
    public readonly days: DayCellVM[];
    public readonly emptyCells: number[];
    public readonly weekDays: WeekDayCellVM[];

    public selectedRange: CalendarRange | undefined;

    public constructor(
        private monthPosition: MonthPosition,
        initialSelectedRange: CalendarRange | undefined,
        onClickDate: (day: Date) => void,
        private availableDates: AvailableDates
    ) {
        this.selectedRange = initialSelectedRange;
        this.emptyCells = this.generateEmptyCells(
            this.getNumberOfEmptyCells(monthPosition.month, monthPosition.year)
        );
        this.days = MonthVM.generateDaysForMonth(
            monthPosition,
            initialSelectedRange,
            availableDates,
            onClickDate
        );
        this.weekDays = this.generateWeekDays();
    }

    private getNumberOfEmptyCells = (month: number, year: number): number => {
        const firstDay = `${year}-${month}-01`;
        const dayIndexInWeek = dayjs(firstDay).day();
        return dayIndexInWeek;
    };

    private generateEmptyCells = (numberOfEmptyCells: number): number[] => {
        const emptyCells: number[] = [];

        for (let i = 0; i < numberOfEmptyCells; i++) {
            emptyCells.push(i);
        }

        return emptyCells;
    };

    private static getSelectionState = (
        selectionRange: CalendarRange | undefined,
        day: Date
    ) => {
        let selectionState = SelectionState.None;

        if (
            selectionRange &&
            !selectionRange.end &&
            selectionRange.isStart(day)
        ) {
            selectionState = SelectionState.Single;
        } else if (selectionRange?.isStart(day)) {
            selectionState = SelectionState.Start;
        } else if (selectionRange?.isEnd(day)) {
            selectionState = SelectionState.End;
        } else if (selectionRange?.contains(day)) {
            selectionState = SelectionState.Middle;
        }

        return selectionState;
    };

    private static generateDaysForMonth = (
        position: MonthPosition,
        selectionRange: CalendarRange | undefined,
        availableDates: AvailableDates,
        onClickDay: (day: Date) => void
    ): DayCellVM[] => {
        const days: DayCellVM[] = [];
        const firstDay = `${position.year}-${position.month}-01`;
        const daysInMonth = dayjs(firstDay).daysInMonth();

        for (let i = 1; i <= daysInMonth; i++) {
            const day = new Date(
                position.year,
                position.month - 1,
                i,
                0,
                0,
                0,
                0
            );

            const selectionState = this.getSelectionState(selectionRange, day);

            days.push(
                new DayCellVM(
                    i.toString(),
                    i.toString(),
                    day,
                    selectionState,
                    availableDates.isAvailable(day),
                    onClickDay
                )
            );
        }

        return days;
    };

    private generateWeekDays = (): WeekDayCellVM[] => {
        return ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map(
            (value) => new WeekDayCellVM(value, value)
        );
    };

    public updateSelection = (newRange: CalendarRange | undefined) => {
        this.days.forEach((day) => {
            const selectionState = MonthVM.getSelectionState(
                newRange,
                day.date
            );

            day.updateSelectionState(selectionState);
        });
    };

    public updateAvailableDates = (availableDates: AvailableDates) => {
        this.days.forEach((day) => {
            day.updateIsAvailable(availableDates.isAvailable(day.date));
        });
    };
}

export default MonthVM;
