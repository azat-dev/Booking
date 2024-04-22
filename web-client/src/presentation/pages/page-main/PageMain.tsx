import React, { useMemo } from "react";

import PropsPageMain from "./props";

import style from "./style.module.scss";
import PageMainViewModelImpl from "./PageMainViewModelImpl";
import NavigationBar from "../../components/navigation-bar/NavigationBar";

const PageMain = (props: PropsPageMain) => {
    const vm = useMemo(() => new PageMainViewModelImpl(), []);

    return (
        <div className={style.pageMain}>
            <NavigationBar vm={vm.navigationBar} />
        </div>
    );
};

export default React.memo(PageMain);
