import React from "react";

import PropsRangePickerCalendar from "./props";
import style from "./style.module.scss";
import MonthView from "../month-view/MonthView";
import { IconButton, Typography } from "@mui/joy";
import NextIcon from "@mui/icons-material/ChevronRight";
import PrevIcon from "@mui/icons-material/ChevronLeft";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const RangePickerCalendar = ({ vm }: PropsRangePickerCalendar) => {
    const [leftMonth, rightMonth, leftMonthTitle, rightMonthTitle] =
        useUpdatesFrom(
            vm.leftMonth,
            vm.rightMonth,
            vm.leftMonthTitle,
            vm.rightMonthTitle
        );
    return (
        <div className={style.rangePickerCalendar}>
            <div className={style.month}>
                <div className={style.header}>
                    <IconButton
                        onClick={vm.showPrev}
                        sx={{
                            position: "absolute",
                            left: 0,
                            top: "50%",
                            transform: "translateY(-50%);",
                        }}
                    >
                        <PrevIcon />
                    </IconButton>
                    <Typography fontSize="h3">{leftMonthTitle}</Typography>
                </div>
                <MonthView vm={leftMonth} />
            </div>
            <div className={style.month}>
                <div className={style.header}>
                    <Typography fontSize="h3">{rightMonthTitle}</Typography>
                    <IconButton
                        onClick={vm.showNext}
                        sx={{
                            position: "absolute",
                            right: 0,
                            top: "50%",
                            transform: "translateY(-50%);",
                        }}
                    >
                        <NextIcon />
                    </IconButton>
                </div>
                <MonthView vm={rightMonth} />
            </div>
        </div>
    );
};

export default React.memo(RangePickerCalendar);
