import React from "react";
import PageMain from "./presentation/pages/page-main/PageMain";
import PageMainViewModelImpl from "./presentation/pages/page-main/PageMainViewModelImpl";
import AuthDialog from "./presentation/dialogs/auth-dialog/AuthDialog";
import AuthDialogViewModel from "./presentation/dialogs/auth-dialog/AuthDialogViewModel";

const App = () => {
    const vm = new PageMainViewModelImpl();
    const vmAuthDialog = new AuthDialogViewModel();
    return (
        <>
            <PageMain vm={vm} />
            <AuthDialog vm={vmAuthDialog} />
        </>
    );
};

export default React.memo(App);
