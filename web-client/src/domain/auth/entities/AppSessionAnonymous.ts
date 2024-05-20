class AppSessionAnonymous {

    public static readonly TYPE: string = 'AppSessionAnonymous';

    public get type() {
        return (this.constructor as any).TYPE;
    }

}

export default AppSessionAnonymous;