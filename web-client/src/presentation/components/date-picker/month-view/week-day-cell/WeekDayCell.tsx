import React from "react";

import PropsWeekDayCell from "./props";
import style from "./style.module.scss";
import { AspectRatio, Box, Typography } from "@mui/joy";

const WeekDayCell = ({ vm }: PropsWeekDayCell) => {
    return (
        <AspectRatio ratio="1/1" variant="plain">
            <Box>
                <Typography
                    fontSize="sm"
                    fontWeight="bold"
                    sx={{ color: "gray" }}
                >
                    {vm.title}
                </Typography>
            </Box>
        </AspectRatio>
    );
};

export default React.memo(WeekDayCell);
