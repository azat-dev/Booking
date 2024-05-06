import React from "react";

import PropsProfileButtonAnonymous from "./props";
import style from "./style.module.scss";
import {
    Dropdown,
    MenuButton,
    Avatar,
    Typography,
    Menu,
    MenuItem,
} from "@mui/joy";
import { mobile } from "../../../utils/selectors";

const ProfileButtonAnonymous = ({ vm }: PropsProfileButtonAnonymous) => {
    return (
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
    );
};

export default React.memo(ProfileButtonAnonymous);
