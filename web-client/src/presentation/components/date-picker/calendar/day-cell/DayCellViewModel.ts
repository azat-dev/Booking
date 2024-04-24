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
    public readonly isDisabled: Subject<boolean>;

    public constructor(
        public readonly id: string,
        public readonly title: string,
        initialSelectionState: SelectionState,
        initialIsDisabled: boolean,
        private onClick: () => void
    ) {
        this.selectionState = value(initialSelectionState);
        this.isDisabled = value(initialIsDisabled);
    }

    public updateSelectionState = (newState: SelectionState): void => {
        this.selectionState.set(newState);
    };

    public updateIsDisabled = (isDisabled: boolean): void => {
        this.isDisabled.set(isDisabled);
    };

    public click = (): void => {
        if (this.isDisabled.value) {
            return;
        }

        this.onClick();
    };
}

export default DayCellViewModel;
