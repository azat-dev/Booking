import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import SearchInputViewModel from "../../components/search-input/SearchInputViewModel";
import ItemsViewModel from "./ItemsViewModel/ItemsViewModel";

export default interface PageMainViewModel {
    navigationBar: NavigationBarViewModel;
    itemsViewModel: ItemsViewModel;
    searchInput: SearchInputViewModel;

    load(): Promise<void>;
}
