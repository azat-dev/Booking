import React from "react";
import PropsActivePage from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";

const ActivePage =({vm, views}: PropsActivePage) =>{
    const [page] = useUpdatesFrom(vm.page);

    if (!page) {
        return null;
    }

    const View = views[page.type as any];

    if (!View) {
        return null;
    }

    return (
        <View vm={page}/>
    );
}

export default React.memo(ActivePage);