import Subject from "../../../../utils/binding/Subject";
import value from "../../../../utils/binding/value";

export enum SelectionState {
    None = "none",
    Single = "single",
    Start = "start",
    End = "end",
    Middle = "middle",
}

class DayCellViewModel {
    public readonly selectionState: Subject<SelectionState>;
    public readonly isAvailable: Subject<boolean>;

    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly date: Date,
        initialSelectionState: SelectionState,
        initialIsAvailable: boolean,
        private onClick: (day: Date) => void
    ) {
        this.selectionState = value(initialSelectionState);
        this.isAvailable = value(!initialIsAvailable);
    }

    public updateSelectionState = (newState: SelectionState): void => {
        this.selectionState.set(newState);
    };

    public updateIsAvailable = (isAvailable: boolean): void => {
        this.isAvailable.set(isAvailable);
    };

    public click = (): void => {
        if (!this.isAvailable.value) {
            return;
        }

        this.onClick(this.date);
    };
}

export default DayCellViewModel;
