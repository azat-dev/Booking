import AccommodiationPreviewViewModel from "../../../components/accommodiation-preview/AccommodiationPreviewViewModel";
import Subject from "../../../utils/binding/Subject";

export interface ItemsViewModelStateLoading {
    type: "loading";
}

export interface ItemsViewModelStateLoaded {
    type: "loaded";
    items: AccommodiationPreviewViewModel[];
    loadMore: (() => Promise<void>) | undefined;
    isLoadingMore: boolean;
}

export type ItemsViewModelState =
    | ItemsViewModelStateLoading
    | ItemsViewModelStateLoaded;

export default interface ItemsViewModel {
    state: Subject<ItemsViewModelState>;
    load(): Promise<void>;
}
