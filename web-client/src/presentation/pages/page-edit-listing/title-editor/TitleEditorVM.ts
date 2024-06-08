import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";

class TitleEditorVM {

    public numberOfCharacters: Subject<number>;
    public maxNumberOfCharacters: number = 40;
    public title: Subject<string>;

    public canMoveNext: Subject<boolean>;

    public constructor(
        initialTitle: string
    ) {
        this.title = value(initialTitle ?? '');
        this.numberOfCharacters = value(initialTitle?.length ?? 0);
        this.canMoveNext = value(false);
        this.updateCanMoveNext();
    }

    private updateCanMoveNext = () => {
        debugger
        this.canMoveNext.set(this.numberOfCharacters.value > 0 && this.numberOfCharacters.value <= this.maxNumberOfCharacters);

    }

    public onChange = (e: any) => {
        const newTitle = e.target.value;
        this.title.set(newTitle);
        this.numberOfCharacters.set(newTitle.length);
        this.updateCanMoveNext();
    }
}

export default TitleEditorVM;