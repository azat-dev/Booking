import AccommodationPreviewViewModel from "../../../components/accommodation-preview/AccommodationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";

export type Item =
    | {
          id: string;
          type: "loading";
      }
    | {
          id: string;
          type: "loaded";
          vm: AccommodationPreviewViewModel;
      };

export interface ItemsViewModelStateNoItems {
    type: "noItems";
}

export interface ShowMoreButtonViewModel {
    click: () => void;
    isLoading: boolean;
}

export interface ItemsViewModelStateShowItems {
    type: "showItems";
    items: Item[];
    showMoreButton?: ShowMoreButtonViewModel;
}

export type ItemsViewModelState =
    | ItemsViewModelStateNoItems
    | ItemsViewModelStateShowItems;

export default interface ItemsViewModel {
    state: Subject<ItemsViewModelState>;
    load(): Promise<void>;
}
