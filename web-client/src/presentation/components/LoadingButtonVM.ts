import Subject from "../utils/binding/Subject.ts";
import value from "../utils/binding/value.ts";
import VM from "../utils/VM.ts";

export default class LoadingButtonVM extends VM {
    public readonly isDisabled: Subject<boolean>;
    public readonly isLoading: Subject<boolean>;

    public constructor(
        initialIsLoading: boolean,
        initialIsDisabled: boolean,
        private onClick: () => void
    ) {
        super();
        this.isLoading = value(initialIsLoading);
        this.isDisabled = value(initialIsDisabled);
    }

    public click = () => {
        this.onClick();
    };

    public updateIsLoading = (isLoading: boolean) => {
        this.isLoading.set(isLoading);
    };

    public updateIsDisabled = (isDisabled: boolean) => {
        this.isDisabled.set(isDisabled);
    };
}