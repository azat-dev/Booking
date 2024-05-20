import NavigationBarVM from "../../components/navigation-bar/NavigationBarVM";
import SearchInputVM from "../../components/search-input/SearchInputVM";
import ItemsVM from "./items-vm/ItemsVM";
import ItemsVMImpl from "./items-vm/ItemsVMImpl";
import PageMainVM from "./PageMainVM";

class PageMainVMImpl extends PageMainVM {
    public readonly itemsVM: ItemsVM;
    public readonly searchInput: SearchInputVM;

    public constructor(
        public readonly navigationBar: NavigationBarVM,
        onToggleFavorite: (id: string) => void
    ) {
        super();
        this.itemsVM = new ItemsVMImpl(onToggleFavorite);
        this.searchInput = new SearchInputVM();
    }

    public load = async (): Promise<void> => {
        await this.itemsVM.load();
    };
}

export default PageMainVMImpl;
