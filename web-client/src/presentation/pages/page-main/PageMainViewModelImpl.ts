import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import SearchInputViewModel from "../../components/search-input/SearchInputViewModel";
import ItemsViewModel from "./ItemsViewModel/ItemsViewModel";
import ItemsViewModelImpl from "./ItemsViewModel/ItemsViewModelImpl";
import PageMainViewModel from "./PageMainViewModel";

class PageMainViewModelImpl implements PageMainViewModel {
    public readonly navigationBar: NavigationBarViewModel;
    public readonly itemsViewModel: ItemsViewModel;
    public readonly searchInput: SearchInputViewModel;

    public constructor() {
        this.navigationBar = new NavigationBarViewModel();
        this.itemsViewModel = new ItemsViewModelImpl();
        this.searchInput = new SearchInputViewModel();
    }

    public load = async (): Promise<void> => {
        await this.itemsViewModel.load();
    };
}

export default PageMainViewModelImpl;