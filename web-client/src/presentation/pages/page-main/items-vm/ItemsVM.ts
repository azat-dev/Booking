import AccommodationPreviewVM from "../../../components/accommodation-preview/AccommodationPreviewVM";
import Subject from "../../../utils/binding/Subject";

export type Item =
    | {
          id: string;
          type: "loading";
      }
    | {
          id: string;
          type: "loaded";
          vm: AccommodationPreviewVM;
      };

export interface ItemsVMStateNoItems {
    type: "noItems";
}

export interface ShowMoreButtonVM {
    click: () => void;
    isLoading: boolean;
}

export interface ItemsVMStateShowItems {
    type: "showItems";
    items: Item[];
    showMoreButton?: ShowMoreButtonVM;
}

export type ItemsVMState =
    | ItemsVMStateNoItems
    | ItemsVMStateShowItems;

export default interface ItemsVM {
    state: Subject<ItemsVMState>;
    load(): Promise<void>;
}
