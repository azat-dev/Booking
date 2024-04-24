import DayCellViewModel, { SelectionState } from "./day-cell/DayCellViewModel";
import dayjs from "dayjs";
import WeekDayCellViewModel from "./week-day-cell/WeekDayCellViewModel";

export enum CellType {
    Day,
    Empty,
}

export type CellViewModel =
    | {
          type: CellType.Empty;
      }
    | {
          type: CellType.Day;
          vm: DayCellViewModel;
      };

export class MonthPosition {
    public constructor(
        public readonly month: number,
        public readonly year: number
    ) {}
}

export class CalendarRange {
    public start: Date;
    public end: Date | undefined;

    private static toStartOfDay = (date: Date): Date =>
        dayjs(date).startOf("day").toDate();

    public constructor(start: Date, end: Date | undefined) {
        this.start = CalendarRange.toStartOfDay(start);
        this.end = end && CalendarRange.toStartOfDay(end);
    }

    public contains = (date: Date): boolean => {
        if (dayjs(date).isBefore(this.start, "day")) {
            return false;
        }

        if (this.end && dayjs(date).isAfter(this.end, "day")) {
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

class MonthViewModel {
    public readonly days: DayCellViewModel[];
    public readonly emptyCells: number[];
    public readonly weekDays: WeekDayCellViewModel[];

    public selectedRange: CalendarRange | undefined;

    public constructor(
        private monthPosition: MonthPosition,
        initialSelectedRange: CalendarRange | undefined
    ) {
        this.selectedRange = initialSelectedRange;
        this.emptyCells = this.generateEmptyCells(
            this.getNumberOfEmptyCells(monthPosition.month, monthPosition.year)
        );
        this.days = MonthViewModel.generateDaysForMonth(
            monthPosition,
            initialSelectedRange
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

    private static generateDaysForMonth = (
        position: MonthPosition,
        selectionRange: CalendarRange | undefined
    ): DayCellViewModel[] => {
        const days: DayCellViewModel[] = [];
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

            let selectionState = SelectionState.None;

            if (selectionRange?.isStart(day)) {
                selectionState = SelectionState.Start;
            } else if (selectionRange?.isEnd(day)) {
                selectionState = SelectionState.End;
            } else if (selectionRange?.contains(day)) {
                selectionState = SelectionState.Middle;
            }

            days.push(
                new DayCellViewModel(
                    i.toString(),
                    i.toString(),
                    selectionState,
                    false,
                    () => {
                        console.log("Day clicked");
                    }
                )
            );
        }

        return days;
    };

    private generateWeekDays = (): WeekDayCellViewModel[] => {
        return ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map(
            (value) => new WeekDayCellViewModel(value, value)
        );
    };
}

export default MonthViewModel;
