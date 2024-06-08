class RowVM {
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly location: string,
        public readonly status: string,
    ) {
    }
}

export default RowVM;