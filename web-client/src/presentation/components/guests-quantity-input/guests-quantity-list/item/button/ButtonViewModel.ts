import Subject from "../../../../../utils/binding/Subject";
import value from "../../../../../utils/binding/value";

class ButtonViewModel {
    public readonly isDisabled: Subject<boolean>;

    public constructor(disabled: boolean, private onClick: () => void) {
        this.isDisabled = value(disabled);
    }

    public updateDisabled = (disabled: boolean): void => {
        this.isDisabled.set(disabled);
    };

    public click = (): void => {
        this.onClick();
    };
}

export default ButtonViewModel;
