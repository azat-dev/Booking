import AccommodationPreviewViewModel from "../../../components/accommodation-preview/AccommodationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import ItemsViewModel, { Item, ItemsViewModelState } from "./ItemsViewModel";

class ItemsViewModelImpl implements ItemsViewModel {
    public state: Subject<ItemsViewModelState>;

    public constructor(private onToggleFavorite: (id: string) => void) {
        this.state = value({
            type: "showItems",
            items: this.generateLoadingItems(20),
        });
    }

    private generateLoadingItems = (count: number) => {
        const items: Item[] = [];

        for (let i = 0; i < count; i++) {
            items.push({
                id: "loading-" + i.toString() + Math.random(),
                type: "loading",
            });
        }
        return items;
    };

    private getItems = () => {
        const picturesIds = [193, 48, 28, 195, 49, 57, 308, 369, 428, 522, 594];

        const items: Item[] = picturesIds.map((id) => {
            const itemId = id.toString() + Math.random();
            return {
                id: itemId,
                type: "loaded",
                vm: new AccommodationPreviewViewModel(
                    itemId,
                    `Hotel ${id}`,
                    `/accommodation/${itemId}`,
                    4.5,
                    "$300",
                    "per night",
                    `https://picsum.photos/id/${id}/400/400`,
                    true,
                    () => {
                        this.onToggleFavorite(itemId);
                    }
                ),
            };
        });

        return items;
    };

    private loadMore = async (): Promise<void> => {
        const currentState = this.state.value;

        const prevItems =
            currentState.type === "showItems" ? currentState.items : [];

        this.state.set({
            type: "showItems",
            items: [
                ...prevItems.filter((i) => i.type != "loading"),
                ...this.generateLoadingItems(15),
            ],
            showMoreButton: { click: () => {}, isLoading: true },
        });

        await new Promise((resolve) => setTimeout(resolve, 1000));
        const newItems = [...prevItems, ...this.getItems()];

        this.state.set({
            type: "showItems",
            items: newItems,
            showMoreButton: {
                click: this.loadMore,
                isLoading: false,
            },
        });
    };

    public load = async (): Promise<void> => {
        this.state.set({
            type: "showItems",
            items: this.generateLoadingItems(15),
            showMoreButton: { click: () => {}, isLoading: true },
        });

        await new Promise((resolve) => setTimeout(resolve, 1000));

        this.state.set({
            type: "showItems",
            items: this.getItems(),
            showMoreButton: { click: this.loadMore, isLoading: false },
        });
    };
}

export default ItemsViewModelImpl;
