import React, { useEffect, useState } from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../utils/binding/useUpdatesFrom";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import PageMain from "../pages/page-main/PageMain";
import { ActiveDialogType } from "./AppViewModel";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";
import PageAccommodationDetails from "../pages/page-accommodation-details/PageAccommodationDetails";
import {
    createBrowserRouter,
    RouterProvider,
    useLocation,
    useParams,
} from "react-router-dom";
import AccommodationId from "../../domain/accommodations/AccommodationId";

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

interface PropsPageComponent {
    Component: any;
    makeVm: (locationParams: any) => Promise<any>;
    LoadingStub: any;
}

const PageComponent = ({
    makeVm,
    Component,
    LoadingStub,
}: PropsPageComponent) => {
    const [vm, setVm] = useState();
    const locationParams = useParams();

    useEffect(() => {
        makeVm(locationParams).then(setVm);
    }, [makeVm, locationParams]);

    if (!vm) {
        return <LoadingStub />;
    }

    return <Component vm={vm} />;
};

const App = ({ vm }: PropsApp) => {
    const router = createBrowserRouter([
        {
            path: "/",
            element: (
                <PageComponent
                    Component={PageMain}
                    makeVm={vm.makeMainPage}
                    LoadingStub={() => <h1>Loading</h1>}
                />
            ),
        },
        {
            path: "/accommodation/:id",
            element: (
                <PageComponent
                    Component={PageAccommodationDetails}
                    makeVm={(params) =>
                        vm.makeAccommodationDetailsPage(
                            new AccommodationId(params.id)
                        )
                    }
                    LoadingStub={() => <h1>Loading</h1>}
                />
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
