import React from "react";

import PropsDateRangePicker from "./props";
import style from "./style.module.scss";
import {
    Box,
    Divider,
    Dropdown,
    Menu,
    MenuButton,
    Stack,
    Typography,
} from "@mui/joy";
import { ClickAwayListener } from "@mui/base/ClickAwayListener";
import RangePickerCalendar from "../range-picker-calendar/RangePickerCalendar";

import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const DateRangePicker = ({ vm }: PropsDateRangePicker) => {
    const [isOpened, isDisabled, checkIn, checkOut] = useUpdatesFrom(
        vm.isOpened,
        vm.isDisabled,
        vm.checkIn,
        vm.checkOut
    );

    return (
        <Dropdown open={isOpened}>
            <MenuButton
                variant="outlined"
                onClick={vm.toggleOpen}
                disabled={isDisabled}
            >
                <Stack direction="row" spacing={1} sx={{ width: "100%" }}>
                    <Stack
                        direction="column"
                        alignItems="flex-start"
                        justifyContent="center"
                        sx={{ width: "50%" }}
                    >
                        <Typography level="body-sm" color="neutral">
                            check-in
                        </Typography>
                        {checkIn && <Typography>{checkIn}</Typography>}
                        {!checkIn && (
                            <Typography sx={{ opacity: 0.4 }}>
                                Add date
                            </Typography>
                        )}
                    </Stack>
                    <Divider orientation="vertical" />
                    <Stack
                        direction="column"
                        alignItems="start"
                        justifyContent="center"
                        sx={{ width: "50%" }}
                    >
                        <Typography level="body-sm" color="neutral">
                            check-out
                        </Typography>
                        {checkOut && <Typography>{checkOut}</Typography>}
                        {!checkOut && (
                            <Typography sx={{ opacity: 0.4 }}>
                                Add date
                            </Typography>
                        )}
                    </Stack>
                </Stack>
            </MenuButton>

            <Menu>
                <ClickAwayListener onClickAway={vm.clickAway}>
                    <Box sx={{ padding: 2, minHeight: 330 }}>
                        <RangePickerCalendar vm={vm.calendar} />
                    </Box>
                </ClickAwayListener>
            </Menu>
        </Dropdown>
    );
};

export default React.memo(DateRangePicker);
