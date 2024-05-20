class AppSessionLoading {

    public static readonly TYPE: string = 'AppSessionLoading';

    public get type() {
        return (this.constructor as any).TYPE;
    }
}

export default AppSessionLoading;