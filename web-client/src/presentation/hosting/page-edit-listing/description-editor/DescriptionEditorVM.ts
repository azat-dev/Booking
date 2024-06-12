import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";

class DescriptionEditorVM {

    public numberOfCharacters: Subject<number>;
    public maxNumberOfCharacters: number = 500;
    public description: Subject<string>;

    public canMoveNext: Subject<boolean>;

    public isLoading: Subject<boolean> = value(true);

    public constructor(
        private readonly getInitialDescription: () => Promise<string>
    ) {
        this.description = value( '');
        this.numberOfCharacters = value(0);
        this.canMoveNext = value(false);
        this.updateCanMoveNext();
    }

    public onChange = (e: any) => {
        const newTitle = e.target.value;
        this.description.set(newTitle);
        this.numberOfCharacters.set(newTitle.length);
        this.updateCanMoveNext();
    }

    private updateCanMoveNext = () => {
        this.canMoveNext.set(this.numberOfCharacters.value > 0 && this.numberOfCharacters.value <= this.maxNumberOfCharacters);

    }

    public load = async () => {
        const initialValue = await this.getInitialDescription();
        this.description.set(initialValue ?? '');
        this.numberOfCharacters.set(initialValue?.length ?? 0);
        this.updateCanMoveNext();
        this.isLoading.set(false);
    }
}

export default DescriptionEditorVM;