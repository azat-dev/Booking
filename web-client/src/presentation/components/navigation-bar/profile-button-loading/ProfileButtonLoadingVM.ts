class ProfileButtonLoadingVM {
    public static readonly TYPE: string = 'ProfileButtonLoadingVM';

    public get type() {
        return (this.constructor as any).TYPE;
    }
}

export default ProfileButtonLoadingVM;