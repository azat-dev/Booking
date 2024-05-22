import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
import SearchInputVM from "../../components/search-input/SearchInputVM";
import ItemsVM from "./items-vm/ItemsVM";
import KeepType from "../../../domain/utils/KeepType.ts";

export default abstract class PageMainVM extends KeepType {

    abstract navigationBar: NavigationBarVM;
    abstract itemsVM: ItemsVM;
    abstract searchInput: SearchInputVM;

    abstract load(): Promise<void>;
}
