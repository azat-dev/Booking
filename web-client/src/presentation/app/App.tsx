import React from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import {ActiveDialogType} from "./app-model/AppViewModel";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";
import PageMain from "../pages/page-main/PageMain";
import PageAccommodationDetails from "../pages/page-accommodation-details/PageAccommodationDetails";
import PageUserProfile from "../pages/page-user-profile/PageUserProfile";
import Router from "./router/Router";
import RouterVM from "./router/RouterVM";
import RouteItem from "./router/RouteItem";
import ActivePage from "./active-page/ActivePage";
import ActivePageVM from "./active-page/ActivePageVM";
import ActiveDialogVM from "./active-dialog/ActiveDialogVM";
import ActiveDialog from "./active-dialog/ActiveDialog";


const App = ({vm}: PropsApp) => {

    const routes: RouteItem[] = [
        {
            path: "/",
            trigger: vm.runMainPage
        },
        {
            path: "/profile",
            trigger: vm.runProfilePage
        },
        {
            path: "/accommodation/:id",
            trigger: (params: any) => vm.runAccommodationDetailsPage(params.id)
        }
    ];

    const router = new RouterVM(routes);

    vm.navigationDelegate = {
        navigateToProfilePage: (replace: boolean) => {
            router.navigate("/profile", replace);
        },
        navigateToMainPage: (replace: boolean) => {
            router.navigate("/", replace);
        }
    };

    const activePage = new ActivePageVM(vm.currentPage);
    const activeDialog = new ActiveDialogVM(vm.activeDialog);

    return (
        <div className={style.app}>
            <Router vm={router}/>
            <ActivePage
                vm={activePage}
                views={{
                    main: PageMain,
                    "accommodation-details": PageAccommodationDetails,
                    "user-profile": PageUserProfile,
                    loading: () => <div>Loading...</div>
                } as any}
            />
            <ActiveDialog
                vm={activeDialog}
                views={{
                    [ActiveDialogType.Login]: LoginDialog,
                    [ActiveDialogType.SignUp]: SignUpDialog
                }}
            />
        </div>
    );
};

export default React.memo(App);
