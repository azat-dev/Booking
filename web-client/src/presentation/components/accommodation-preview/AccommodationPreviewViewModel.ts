class AccommodationPreviewViewModel {
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly rating: number,
        public readonly price: string,
        public readonly unit: string,
        public readonly image: string,
        public readonly isFavorite: boolean,
        private onToggleFavorite: () => void
    ) {}

    public toggleFavorite = (): void => {
        this.onToggleFavorite();
    };
}

export default AccommodationPreviewViewModel;
