import React, { useMemo } from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../utils/binding/useUpdatesFrom";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import PageMain from "../pages/page-main/PageMain";
import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";
import { ActiveDialogType } from "./AppViewModel";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";

const dialogTypes: any = {
    [ActiveDialogType.Login]: LoginDialog,
    [ActiveDialogType.SignUp]: SignUpDialog,
};

const App = ({ vm }: PropsApp) => {
    const [activeDialog] = useUpdatesFrom(vm.activeDialog);
    const pageMain = useMemo(
        () =>
            new PageMainViewModelImpl(
                vm.openLoginDialog,
                vm.openSignUpDialog,
                vm.toggleFavorite
            ),
        []
    );

    const Dialog = dialogTypes[activeDialog?.type];

    return (
        <div className={style.app}>
            <PageMain vm={pageMain} />
            {activeDialog && <Dialog vm={activeDialog!.vm} />}
        </div>
    );
};

export default React.memo(App);
