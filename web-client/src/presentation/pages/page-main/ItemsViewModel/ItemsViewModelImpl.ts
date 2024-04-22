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

        const picturesIds = [193, 48, 28, 195, 49, 57, 308, 369, 428, 522, 594];

        const items: AccommodiationPreviewViewModel[] = picturesIds.map(
            (id) =>
                new AccommodiationPreviewViewModel(
                    id.toString(),
                    `Hotel ${id}`,
                    4.5,
                    300,
                    `https://picsum.photos/id/${id}/400/400`
                )
        );

        this.state.set({ type: "loaded", items });
    };
}

export default ItemsViewModelImpl;
