import NavigationBarViewModel from "../../components/navigation-bar/NavigationBarViewModel";
import ItemsViewModel from "./ItemsViewModel/ItemsViewModel";

export default interface PageMainViewModel {
    navigationBar: NavigationBarViewModel;
    itemsViewModel: ItemsViewModel;

    load(): Promise<void>;
}
