import React from "react";

import PropsMonthView from "./props";
import style from "./style.module.scss";
import { Box, Grid } from "@mui/joy";
import DayCell from "./day-cell/DayCell";
import WeekDayCell from "./week-day-cell/WeekDayCell";
import EmptyCell from "./empty-cell/EmptyCell";

const MonthView = ({ vm }: PropsMonthView) => {
    return (
        <Box sx={{ maxWidth: 300, minWidth: 300, width: 300 }}>
            <Grid container columns={7} spacing={0}>
                {vm.weekDays.map((weekDay) => (
                    <Grid
                        key={weekDay.id}
                        className={style.weekDay}
                        lg={1}
                        sm={1}
                        xs={1}
                        xl={1}
                        md={1}
                    >
                        <WeekDayCell vm={weekDay} />
                    </Grid>
                ))}
                {vm.emptyCells.map((id) => (
                    <Grid
                        key={"empty" + id}
                        className={style.weekDay}
                        lg={1}
                        sm={1}
                        xs={1}
                        xl={1}
                        md={1}
                    >
                        <EmptyCell />
                    </Grid>
                ))}
                {vm.days.map((day) => (
                    <Grid
                        key={day.id}
                        className={style.weekDay}
                        lg={1}
                        sm={1}
                        xs={1}
                        xl={1}
                        md={1}
                    >
                        <DayCell vm={day} />
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
};

export default React.memo(MonthView);
