import AccommodiationPreviewViewModel from "../../../components/accommodiation-preview/AccommodiationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import ItemsViewModel, { ItemsViewModelState } from "./ItemsViewModel";

class ItemsViewModelImpl implements ItemsViewModel {
    public state: Subject<ItemsViewModelState>;

    public constructor() {
        this.state = value({ type: "loading" });
    }

    private appendItems = async (): Promise<void> => {
        const currentState = this.state.value;

        switch (currentState.type) {
            case "loading":
                break;
            case "loaded":
                this.state.set({
                    ...currentState,
                    isLoadingMore: true,
                });
                break;
        }

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

        const newItems = [
            ...(this.state.value.type === "loaded"
                ? this.state.value.items
                : []),
            ...items,
        ];

        this.state.set({
            type: "loaded",
            items: newItems,
            loadMore: this.appendItems,
            isLoadingMore: false,
        });
    };

    public load = async (): Promise<void> => {
        this.state.set({ type: "loading" });

        this.appendItems();
    };
}

export default ItemsViewModelImpl;
