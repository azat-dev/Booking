import React from "react";
import "reflect-metadata";


import PropsApp from "./props";
import style from "./style.module.scss";
import Router from "./router/Router";
import ActiveDialog from "./active-dialog/ActiveDialog";
import LoginDialogVM from "../dialogs/login-dialog/LoginDialogVM";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import SignUpDialogVM from "../dialogs/sign-up-dialog/SignUpDialogVM";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";

const App = ({vm}: PropsApp) => {

    return (
        <div className={style.app}>
            <Router vm={vm.router}/>
            <ActiveDialog
                vm={vm.activeDialog}
                views={{
                    [LoginDialogVM.type]: LoginDialog,
                    [SignUpDialogVM.type]: SignUpDialog
                }}
            />
        </div>
    );
};

export default React.memo(App);
