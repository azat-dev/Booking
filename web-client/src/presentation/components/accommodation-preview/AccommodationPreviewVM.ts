import VM from "../../utils/VM.ts";

class AccommodationPreviewVM extends VM {
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly link: string,
        public readonly rating: number,
        public readonly price: string,
        public readonly unit: string,
        public readonly image: string,
        public readonly isFavorite: boolean,
        private onToggleFavorite: () => void
    ) {

        super();
    }

    public toggleFavorite = (): void => {
        this.onToggleFavorite();
    };
}

export default AccommodationPreviewVM;
