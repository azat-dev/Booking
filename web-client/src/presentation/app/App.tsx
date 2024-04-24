import React from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../utils/binding/useUpdatesFrom";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import PageMain from "../pages/page-main/PageMain";
import { ActiveDialogType } from "./AppViewModel";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";
import PageAccommodationDetails from "../pages/page-accommodation-details/PageAccommodationDetails";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const dialogTypes: any = {
    [ActiveDialogType.Login]: LoginDialog,
    [ActiveDialogType.SignUp]: SignUpDialog,
};

const Dialogs = ({ vm }: PropsApp) => {
    const [activeDialog] = useUpdatesFrom(vm.activeDialog);

    if (!activeDialog) {
        return null;
    }

    const Dialog = dialogTypes[activeDialog?.type];
    return <Dialog vm={activeDialog!.vm} />;
};

const makePageComponent = (Component: any, makeVm: () => any) => {
    return () => {
        return <Component vm={makeVm()} />;
    };
};

const App = ({ vm }: PropsApp) => {
    const router = createBrowserRouter([
        {
            path: "/",
            Component: makePageComponent(PageMain, vm.makeMainPage),
        },
        {
            path: "/accommodation/:id",
            Component: makePageComponent(
                PageAccommodationDetails,
                vm.makeAccommodationDetailsPage
            ),
        },
    ]);

    return (
        <div className={style.app}>
            <RouterProvider router={router}></RouterProvider>;
            <Dialogs vm={vm} />
        </div>
    );
};

export default React.memo(App);
