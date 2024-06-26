import React from "react";
import PropsActiveDialog from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";

const ActiveDialog = ({vm, views}: PropsActiveDialog) => {
    const [dialog] = useUpdatesFrom(vm);

    if (!dialog) {
        return null;
    }

    const View = views[dialog.type as any];
    console.log("ActiveDialog", dialog.type, dialog, View);
    if (!View) {
        return null;
    }
    return (
        <View vm={dialog}/>
    );
}

export default React.memo(ActiveDialog);