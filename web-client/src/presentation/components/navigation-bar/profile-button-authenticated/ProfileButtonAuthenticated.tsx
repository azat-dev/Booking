import React from "react";

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
import PropsProfileButtonAuthenticated from "./props";
import AvatarButton from "./avatar-button/AvatarButton";

const ProfileButtonAuthenticated = ({
    vm,
}: PropsProfileButtonAuthenticated) => {
    return (
        <Dropdown>
            <MenuButton
                variant="plain"
                size="lg"
                sx={{
                    maxHeight: "48px",
                    borderRadius: "9999999px",
                    backgroundColor: "transparent",
                }}
                startDecorator={<AvatarButton vm={vm.avatar} />}
            ></MenuButton>
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
                <MenuItem onClick={vm.openProfile}>
                    <strong>Profile</strong>
                </MenuItem>
                <MenuItem onClick={vm.logout}>Logout</MenuItem>
            </Menu>
        </Dropdown>
    );
};

export default React.memo(ProfileButtonAuthenticated);
