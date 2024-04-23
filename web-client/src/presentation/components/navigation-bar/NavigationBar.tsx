import * as React from "react";
import { Box, IconButton, Menu, MenuButton, MenuItem, Theme } from "@mui/joy";
import Typography from "@mui/joy/Typography";

import Dropdown from "@mui/joy/Dropdown";
import Avatar from "@mui/joy/Avatar";
import MapsHomeWorkIcon from "@mui/icons-material/MapsHomeWork";

import PropsNavigationBar from "./props";

import style from "./style.module.scss";
import { mobile, tablet, desktop } from "../../utils/selectors";

const NavigationBar = ({ vm }: PropsNavigationBar) => {
    return (
        <Box
            sx={(theme) => ({
                display: "flex",
                flexDirection: "row",
                justifyContent: "space-between",
                alignItems: "center",
                width: "100%",
                boxSizing: "border-box",
                top: 0,
                px: 1.5,
                py: 1,
                zIndex: 1000,
                position: "sticky",
                [mobile(theme)]: {
                    backgroundColor: "background.surface",
                },
                [tablet(theme)]: {
                    backgroundColor: "background.surface",
                    paddingLeft: 5,
                    paddingRight: 5,
                },
                [desktop(theme)]: {
                    paddingLeft: 10,
                    paddingRight: 10,
                    paddingTop: 2,
                    backgroundColor: "background.body",
                },
            })}
        >
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                    justifyContent: "space-between",
                    gap: 1.5,
                }}
            >
                <IconButton size="sm" variant="soft" color="primary">
                    <MapsHomeWorkIcon />
                </IconButton>
                <Typography component="h1" fontWeight="xl" color="primary">
                    Demo Booking
                </Typography>
            </Box>
            <Dropdown>
                <MenuButton
                    variant="outlined"
                    size="lg"
                    sx={{
                        maxHeight: "48px",
                        borderRadius: "9999999px",
                        backgroundColor: "transparent",
                    }}
                    startDecorator={
                        <Avatar
                            sx={{ maxWidth: "48px", maxHeight: "48px" }}
                            variant="plain"
                        />
                    }
                >
                    <Typography
                        sx={(theme) => {
                            return {
                                [mobile(theme)]: {
                                    display: "none",
                                },
                            };
                        }}
                    >
                        Login / Sign Up
                    </Typography>
                </MenuButton>
                <Menu
                    placement="bottom-end"
                    size="sm"
                    sx={{
                        zIndex: "99999",
                        p: 1,
                        gap: 1,
                        "--ListItem-radius": "var(--joy-radius-sm)",
                        minWidth: "200px",
                    }}
                >
                    <MenuItem onClick={vm.signUp}>
                        <strong>Sign Up</strong>
                    </MenuItem>
                    <MenuItem onClick={vm.login}>Log In</MenuItem>
                </Menu>
            </Dropdown>
        </Box>
    );
};

export default React.memo(NavigationBar);
