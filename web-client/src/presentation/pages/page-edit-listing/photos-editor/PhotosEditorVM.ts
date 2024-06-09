import Subject from "../../../utils/binding/Subject.ts";
import value from "../../../utils/binding/value.ts";
import InitialPhotoInputVM from "./initial-photo-input/InitialPhotoInputVM.ts";

class PhotosEditorVM {

    public numberOfCharacters: Subject<number>;
    public maxNumberOfCharacters: number = 500;
    public description: Subject<string>;

    public initialPhotoInput: Subject<InitialPhotoInputVM>;
    public canMoveNext: Subject<boolean>;

    public constructor(
        initialValue: string
    ) {
        this.initialPhotoInput = value(new InitialPhotoInputVM());
        this.description = value(initialValue ?? '');
        this.numberOfCharacters = value(initialValue?.length ?? 0);
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
}

export default PhotosEditorVM;