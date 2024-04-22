import React from "react";
import PageMain from "./presentation/pages/page-main/PageMain";
import PageMainViewModelImpl from "./presentation/pages/page-main/PageMainViewModelImpl";
import AuthDialog from "./presentation/dialogs/auth-dialog/AuthDialog";
import AuthDialogViewModel from "./presentation/dialogs/auth-dialog/AuthDialogViewModel";
import LocalStorageTokensRepository from "./LocalStorageTokensRepository";
import CurrentSessionStoreImpl from "./domain/auth/CurrentSession/CurrentSessionStoreImpl";
import AuthServiceImpl from "./AuthServiceImpl";

const App = () => {
    const tokensRepository = new LocalStorageTokensRepository();
    const authService = new AuthServiceImpl();
    const currentSessionStore = new CurrentSessionStoreImpl(
        tokensRepository,
        authService
    );
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
