import React from "react";
import PageMain from "./presentation/pages/page-main/PageMain";
import PageMainViewModelImpl from "./presentation/pages/page-main/PageMainViewModelImpl";

const App = () => {
    const vm = new PageMainViewModelImpl();
    return <PageMain vm={vm} />;
};

export default React.memo(App);
