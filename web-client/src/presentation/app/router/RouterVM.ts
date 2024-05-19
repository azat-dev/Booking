import {createBrowserRouter} from "react-router-dom";
import RouteItem from "./RouteItem";

class RouterVM {

    public reactRouter: any;

    constructor(routes: RouteItem[]) {

        const mappedRoutes = routes.map((route) => {
            return {
                path: route.path,
                loader: async (params: any) => {
                    await route.trigger(params);
                    return null;
                }
            };
        });

        this.reactRouter = createBrowserRouter(mappedRoutes);
    }

    public navigate = (path: string, replace: boolean = false): void => {
        this.reactRouter.navigate(path, {replace});
    }
}

export default RouterVM;