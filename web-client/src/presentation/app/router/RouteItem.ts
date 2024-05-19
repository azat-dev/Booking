interface RouteItem {
    path: string;
    trigger: ((params: any) => Promise<void>) | (() => Promise<void>);
}

export default RouteItem;