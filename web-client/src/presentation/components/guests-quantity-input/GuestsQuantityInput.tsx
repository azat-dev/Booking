import React from "react";

import PropsGuestsQuantityInput from "./props";
import style from "./style.module.scss";
import { Dropdown, Menu, Stack, Typography, MenuButton } from "@mui/joy";
import GuestsQuantityList from "./guests-quantity-list/GuestsQuantityList";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";

const GuestsQuantityInput = ({ vm }: PropsGuestsQuantityInput) => {
    const [guestsSummary] = useUpdatesFrom(vm.guestsSummary);
    return (
        <Dropdown>
            <MenuButton variant="outlined" sx={{ width: "100%" }}>
                <Stack
                    direction="column"
                    sx={{ width: "100%" }}
                    alignItems="flex-start"
                    justifyContent="center"
                >
                    <Typography level="body-md">Guests</Typography>
                    <Typography level="body-sm">{guestsSummary}</Typography>
                </Stack>
            </MenuButton>
            <Menu>
                <GuestsQuantityList vm={vm.guestsQuantityList} />
            </Menu>
        </Dropdown>
    );
};

export default React.memo(GuestsQuantityInput);
