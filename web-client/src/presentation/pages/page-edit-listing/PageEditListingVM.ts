import TitleEditorVM from "./title-editor/TitleEditorVM.ts";
import Subject from "../../utils/binding/Subject.ts";
import value from "../../utils/binding/value.ts";

export type Content = TitleEditorVM;

class PageEditListingVM {

    public content: Subject<Content>;
    public isNextButtonEnabled: Subject<boolean> = value(true);

    public constructor() {
        const editor = new TitleEditorVM();
        this.isNextButtonEnabled.set(editor.canMoveNext.value);
        editor.canMoveNext.listen(this.isNextButtonEnabled.set);
        this.content = value(editor);
    }
}

export default PageEditListingVM;