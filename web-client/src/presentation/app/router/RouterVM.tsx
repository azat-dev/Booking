import {createBrowserRouter, useLoaderData} from "react-router-dom";
import RouteItem from "./RouteItem";
import AppSession from "../../../domain/auth/entities/AppSession.ts";
import React from "react";
import PagesConfig from "../config/PagesConfig.ts";

const RouteComponent = React.memo(() => {

    return  useLoaderData() as any;
});

RouteComponent.displayName = "RouteComponent";

class RouterVM {

    public reactRouter: any;

    constructor(routes: RouteItem[], appSession: AppSession, pagesConfig: PagesConfig) {

        const mappedRoutes = routes.map((route) => {
            return {
                path: route.path,
                loader: async (params: any) => {
                    return await route.view(appSession.state, params, pagesConfig);
                },
                element: <RouteComponent/>,
            };
        });

        this.reactRouter = createBrowserRouter(mappedRoutes);
    }

    public navigate = (path: string, replace: boolean = false): void => {
        this.reactRouter.navigate(path, {replace});
    }
}

export default RouterVM;