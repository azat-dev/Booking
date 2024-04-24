import React from "react";

import PropsRangePickerCalendar from "./props";
import style from "./style.module.scss";
import MonthView from "../month-view/MonthView";
import { Typography } from "@mui/joy";

const RangePickerCalendar = ({ vm }: PropsRangePickerCalendar) => {
    return (
        <div className={style.rangePickerCalendar}>
            <div className={style.month}>
                <div className={style.header}>
                    <Typography fontSize="h3">May 2025</Typography>
                </div>
                <MonthView vm={vm.leftMonth} />
            </div>
            <div className={style.month}>
                <div className={style.header}>
                    <Typography fontSize="h3">May 2025</Typography>
                </div>
                <MonthView vm={vm.rightMonth} />
            </div>
        </div>
    );
};

export default React.memo(RangePickerCalendar);
