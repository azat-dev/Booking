class AccommodiationPreviewViewModel {
    public price: string;
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly rating: number,
        price: number,
        public readonly image: string
    ) {
        this.price = `$${price} per night`;
    }
}

export default AccommodiationPreviewViewModel;
