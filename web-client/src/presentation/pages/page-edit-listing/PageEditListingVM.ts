import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import Subject from "../../utils/binding/Subject.ts";
import value from "../../utils/binding/value.ts";
import DescriptionEditorVM from "./description-editor/DescriptionEditorVM.ts";
import PhotosEditorVM from "./photos-editor/PhotosEditorVM.ts";

export type Content = TitleEditorVM | DescriptionEditorVM | PhotosEditorVM;

class PageEditListingVM {

    public content: Subject<Content>;
    public isNextButtonEnabled: Subject<boolean> = value(true);

    public constructor() {
        const editor = new PhotosEditorVM('initial value');
        this.isNextButtonEnabled.set(editor.canMoveNext.value);
        editor.canMoveNext.listen(this.isNextButtonEnabled.set);
        this.content = value(editor);
    }

    public moveToNextStep = () => {

    }
}

export default PageEditListingVM;