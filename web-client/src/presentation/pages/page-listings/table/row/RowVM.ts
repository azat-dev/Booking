class RowVM {
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly location: string | undefined,
        public readonly status: string,
    ) {
    }
}

export default RowVM;