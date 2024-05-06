import CurrentSessionStore from "../../../domain/auth/CurrentSession/CurrentSessionStore";
import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import ProfileButtonAnonymousViewModel from "../../components/navigation-bar/profile-button-anonymous/ProfileButtonAnonymousViewModel";
import SearchInputViewModel from "../../components/search-input/SearchInputViewModel";
import ItemsViewModel from "./ItemsViewModel/ItemsViewModel";
import ItemsViewModelImpl from "./ItemsViewModel/ItemsViewModelImpl";
import PageMainViewModel from "./PageMainViewModel";

class PageMainViewModelImpl implements PageMainViewModel {
    public readonly itemsViewModel: ItemsViewModel;
    public readonly searchInput: SearchInputViewModel;

    public constructor(
        public readonly navigationBar: NavigationBarViewModel,
        onToggleFavorite: (id: string) => void
    ) {
        this.itemsViewModel = new ItemsViewModelImpl(onToggleFavorite);
        this.searchInput = new SearchInputViewModel();
    }

    public load = async (): Promise<void> => {
        await this.itemsViewModel.load();
    };
}

export default PageMainViewModelImpl;
