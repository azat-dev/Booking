import React, { useMemo } from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../utils/binding/useUpdatesFrom";
import AuthDialog from "../dialogs/auth-dialog/AuthDialog";
import PageMain from "../pages/page-main/PageMain";
import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";

const App = ({ vm }: PropsApp) => {
    const [activeDialog] = useUpdatesFrom(vm.activeDialog);
    const pageMain = useMemo(
        () => new PageMainViewModelImpl(vm.openLoginDialog, vm.toggleFavorite),
        []
    );
    return (
        <div className={style.app}>
            <PageMain vm={pageMain} />
            {activeDialog && activeDialog.type === "login" && (
                <AuthDialog vm={activeDialog.vm} />
            )}
        </div>
    );
};

export default React.memo(App);
