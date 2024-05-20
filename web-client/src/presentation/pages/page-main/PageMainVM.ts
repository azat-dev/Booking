import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
import SearchInputVM from "../../components/search-input/SearchInputVM";
import ItemsVM from "./items-vm/ItemsVM";

export default interface PageMainVM {
    navigationBar: NavigationBarVM;
    itemsVM: ItemsVM;
    searchInput: SearchInputVM;

    load(): Promise<void>;
}
