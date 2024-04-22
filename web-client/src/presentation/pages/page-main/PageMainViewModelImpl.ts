import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import PageMainViewModel from "./PageMainViewModel";

class PageMainViewModelImpl implements PageMainViewModel {
    public navigationBar: NavigationBarViewModel;

    constructor() {
        this.navigationBar = new NavigationBarViewModel();
    }
}

export default PageMainViewModelImpl;
