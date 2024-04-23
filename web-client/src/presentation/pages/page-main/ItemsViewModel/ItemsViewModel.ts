import AccommodiationPreviewViewModel from "../../../components/accommodiation-preview/AccommodiationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";

export type Item =
    | {
          id: string;
          type: "loading";
      }
    | {
          id: string;
          type: "loaded";
          vm: AccommodiationPreviewViewModel;
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
