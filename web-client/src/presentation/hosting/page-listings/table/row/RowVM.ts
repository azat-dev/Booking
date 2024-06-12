class RowVM {
    public constructor(
        public readonly id: string,
        public readonly title: string,
        public readonly location: string | undefined,
        public readonly status: string,
        public readonly click: () => void
    ) {
    }
}

export default RowVM;