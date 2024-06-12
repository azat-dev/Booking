import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";

class TitleEditorVM {

    public numberOfCharacters: Subject<number>;
    public maxNumberOfCharacters: number = 40;
    public title: Subject<string>;
    public isLoading: Subject<boolean>;

    public canMoveNext: Subject<boolean>;

    public constructor(
        private readonly getInitialListingTitle: () => Promise<string>
    ) {
        this.title = value('');
        this.isLoading = value(true);
        this.numberOfCharacters = value(0);
        this.canMoveNext = value(false);
    }

    private updateCanMoveNext = () => {
        this.canMoveNext.set(this.numberOfCharacters.value > 0 && this.numberOfCharacters.value <= this.maxNumberOfCharacters);

    }

    public onChange = (e: any) => {
        const newTitle = e.target.value;
        this.title.set(newTitle);
        this.numberOfCharacters.set(newTitle.length);
        this.updateCanMoveNext();
    }

    public load = async () => {
        const initialTitle = await this.getInitialListingTitle();
        this.title.set(initialTitle);
        this.numberOfCharacters.set(initialTitle.length);
        this.isLoading.set(false);
        this.updateCanMoveNext();
    }
}

export default TitleEditorVM;