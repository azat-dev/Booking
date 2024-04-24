import React from "react";

import PropsDayCell from "./props";
import style from "./style.module.scss";
import Box from "@mui/joy/Box";
import { AspectRatio, Button, Typography } from "@mui/joy";
import useUpdatesFrom from "../../../../utils/binding/useUpdatesFrom";
import { SelectionState } from "./DayCellViewModel";

const DayCell = ({ vm }: PropsDayCell) => {
    const [isDisabled, selectionState] = useUpdatesFrom(
        vm.isDisabled,
        vm.selectionState
    );

    let titleColor = "inherit";
    if (selectionState === SelectionState.Single) {
        titleColor = "white";
    }

    return (
        <AspectRatio ratio="1/1" variant="plain">
            <div className={style.dayCell} data-selection={selectionState}>
                <div
                    className={style.circle}
                    onClick={vm.click}
                    data-disabled={!!isDisabled}
                    data-selected={[
                        SelectionState.Single,
                        SelectionState.Start,
                        SelectionState.End,
                    ].includes(selectionState)}
                >
                    <Typography
                        fontSize="sm"
                        fontWeight="md"
                        sx={{ color: "inherit" }}
                    >
                        {vm.title}
                    </Typography>
                </div>
            </div>
        </AspectRatio>
    );
};

export default React.memo(DayCell);
