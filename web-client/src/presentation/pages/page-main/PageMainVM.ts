import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
import SearchInputVM from "../../components/search-input/SearchInputVM";
import ItemsVM from "./items-vm/ItemsVM";
import Page from "../../app/Page";

export default abstract class PageMainVM extends Page {

    public static readonly TYPE = "PAGE_MAIN_VM";

    public get type() {
        return (this.constructor as any).TYPE;
    }

    abstract navigationBar: NavigationBarVM;
    abstract itemsVM: ItemsVM;
    abstract searchInput: SearchInputVM;

    abstract load(): Promise<void>;
}
