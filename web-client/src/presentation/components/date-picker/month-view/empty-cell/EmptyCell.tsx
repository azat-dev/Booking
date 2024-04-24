import React from "react";

import PropsEmptyCell from "./props";
import style from "./style.module.scss";
import AspectRatio from "@mui/joy/AspectRatio";
import Box from "@mui/joy/Box";

const EmptyCell = (props: PropsEmptyCell) => {
    return (
        <AspectRatio ratio="1/1" variant="plain">
            <Box></Box>
        </AspectRatio>
    );
};

export default React.memo(EmptyCell);
