import React from "react";

import PropsButton from "./props";
import style from "./style.module.scss";
import { IconButton } from "@mui/joy";
import useUpdatesFrom from "../../../../../utils/binding/useUpdatesFrom";

const Button = ({ vm, icon }: PropsButton) => {
    const [isDisabled] = useUpdatesFrom(vm.isDisabled);
    return (
        <IconButton
            disabled={isDisabled}
            size="sm"
            sx={{
                border: "1px solid #dddddd",
                borderRadius: "50%",
                padding: 1,
                boxSizing: "border-box",
                maxHeight: 32,
                maxWidth: 32,
            }}
        >
            {icon}
        </IconButton>
    );
};

export default React.memo(Button);
