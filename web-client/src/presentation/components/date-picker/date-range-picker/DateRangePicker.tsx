import React from "react";

import PropsDateRangePicker from "./props";
import style from "./style.module.scss";
import {
    Box,
    Divider,
    Dropdown,
    Input,
    Menu,
    MenuButton,
    Stack,
    Typography,
} from "@mui/joy";
import RangePickerCalendar from "../range-picker-calendar/RangePickerCalendar";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const DateRangePicker = ({ vm }: PropsDateRangePicker) => {
    const [checkIn, checkOut] = useUpdatesFrom(vm.checkIn, vm.checkOut);
    return (
        <Dropdown>
            <MenuButton variant="outlined">
                <Stack direction="row" spacing={1}>
                    <Stack
                        direction="column"
                        alignItems="flex-start"
                        justifyContent="center"
                    >
                        <Typography level="body-sm" color="neutral">
                            check-in
                        </Typography>
                        {checkIn && <Typography>{checkIn}</Typography>}
                        {!checkIn && <Typography>Add check in</Typography>}
                    </Stack>
                    <Divider orientation="vertical" />
                    <Stack
                        direction="column"
                        alignItems="start"
                        justifyContent="center"
                    >
                        <Typography level="body-sm" color="neutral">
                            check-out
                        </Typography>
                        {checkOut && <Typography>{checkOut}</Typography>}
                        {!checkOut && <Typography>Add check out</Typography>}
                    </Stack>
                </Stack>
            </MenuButton>
            <Menu>
                <Box sx={{ padding: 2, minHeight: 330 }}>
                    <RangePickerCalendar vm={vm.calendar} />
                </Box>
            </Menu>
        </Dropdown>
    );
};

export default React.memo(DateRangePicker);
