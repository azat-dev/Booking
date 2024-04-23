import AccommodiationPreviewViewModel from "../../../components/accommodiation-preview/AccommodiationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import ItemsViewModel, { ItemsViewModelState } from "./ItemsViewModel";

class ItemsViewModelImpl implements ItemsViewModel {
    public state: Subject<ItemsViewModelState>;

    public constructor() {
        this.state = value({ type: "loading" });
    }

    private getItems = () => {
        const picturesIds = [193, 48, 28, 195, 49, 57, 308, 369, 428, 522, 594];

        const items: AccommodiationPreviewViewModel[] = picturesIds.map(
            (id) =>
                new AccommodiationPreviewViewModel(
                    id.toString() + Math.random(),
                    `Hotel ${id}`,
                    4.5,
                    300,
                    `https://picsum.photos/id/${id}/400/400`
                )
        );

        return items;
    };

    private loadMore = async (): Promise<void> => {
        const currentState = this.state.value;

        if (currentState.type === "loaded") {
            this.state.set({
                ...currentState,
                isLoadingMore: true,
            });
        }

        const newItems = [
            ...(this.state.value.type === "loaded"
                ? this.state.value.items
                : []),
            ...this.getItems(),
        ];

        this.state.set({
            type: "loaded",
            items: newItems,
            loadMore: this.loadMore,
            isLoadingMore: false,
        });
    };

    public load = async (): Promise<void> => {
        this.state.set({ type: "loading" });
        this.state.set({
            type: "loaded",
            items: this.getItems(),
            loadMore: this.loadMore,
            isLoadingMore: false,
        });
    };
}

export default ItemsViewModelImpl;
