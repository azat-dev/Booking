import React from "react";
import {RouterProvider} from "react-router-dom";
import RouterVM from "./RouterVM";

const Router = ({vm}: { vm: RouterVM }) => {
    return (
        <RouterProvider router={vm.reactRouter}/>
    );
}

export default React.memo(Router);