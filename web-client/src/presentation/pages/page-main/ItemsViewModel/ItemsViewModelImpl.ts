import AccommodiationPreviewViewModel from "../../../components/accommodiation-preview/AccommodiationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import ItemsViewModel, { ItemsViewModelState } from "./ItemsViewModel";

class ItemsViewModelImpl implements ItemsViewModel {
    public state: Subject<ItemsViewModelState>;

    public constructor() {
        this.state = value({ type: "loading" });
    }

    public load = async (): Promise<void> => {
        this.state.set({ type: "loading" });

        const items: AccommodiationPreviewViewModel[] = [
            new AccommodiationPreviewViewModel(
                "1",
                "Hotel 1",
                4.5,
                300,
                "https://picsum.photos/id/237/400/400"
            ),
            // new AccommodiationPreviewViewModel(
            //     "1",
            //     "Hotel 2",
            //     4.0,
            //     200,
            //     "https://picsum.photos/id/238/400/400"
            // ),
            // new AccommodiationPreviewViewModel(
            //     "1",
            //     "Hotel 3",
            //     3.5,
            //     100,
            //     "https://picsum.photos/id/239/400/400"
            // ),
        ];

        this.state.set({ type: "loaded", items });
    };
}

export default ItemsViewModelImpl;
