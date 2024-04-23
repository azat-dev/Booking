class AccommodiationPreviewViewModel {
    public price: string;
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly rating: number,
        price: number,
        public readonly image: string,
        public readonly isFavorite: boolean,
        private onToggleFavorite: () => void
    ) {
        this.price = `$${price} per night`;
    }

    public toggleFavorite = (): void => {
        this.onToggleFavorite();
    };
}

export default AccommodiationPreviewViewModel;
