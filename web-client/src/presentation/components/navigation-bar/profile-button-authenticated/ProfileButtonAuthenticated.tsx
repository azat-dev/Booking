import React from "react";
import {Dropdown, ListDivider, Menu, MenuButton, MenuItem, Stack, Typography,} from "@mui/joy";
import PropsProfileButtonAuthenticated from "./props";
import AvatarButton from "./avatar-button/AvatarButton";
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import HelpRoundedIcon from '@mui/icons-material/HelpRounded';
import OpenInNewRoundedIcon from '@mui/icons-material/OpenInNewRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import Box from "@mui/joy/Box";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const ProfileButtonAuthenticated = ({vm}: PropsProfileButtonAuthenticated) => {

    const [email, fullName] = useUpdatesFrom(vm.email, vm.fullName);
    return (
        <Dropdown>
            <MenuButton
                variant="outlined"
                size="sm"
                sx={{maxWidth: '300px', maxHeight: '48px', borderRadius: '9999999px'}}
            >
                <Box
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                    }}
                >
                    <AvatarButton vm={vm.avatar}/>
                    <Stack sx={{ml: 1.5}} direction="column" alignItems="flex-start">
                        <Typography level="title-sm" textColor="text.primary">
                            {fullName}
                        </Typography>
                        <Typography level="body-xs" textColor="text.tertiary">
                            {email}
                        </Typography>
                    </Stack>
                </Box>
            </MenuButton>
            <Menu
                placement="bottom-end"
                size="sm"
                sx={{
                    zIndex: '99999',
                    p: 1,
                    gap: 1,
                    '--ListItem-radius': 'var(--joy-radius-sm)',
                }}
            >
                <MenuItem>
                    <HelpRoundedIcon/>
                    Help
                </MenuItem>
                <MenuItem>
                    <SettingsRoundedIcon/>
                    Profile
                </MenuItem>
                <ListDivider/>
                <MenuItem component="a" href="/blog/first-look-at-joy/">
                    First look at Joy UI
                    <OpenInNewRoundedIcon/>
                </MenuItem>
                <MenuItem
                    component="a"
                    href="https://github.com/mui/material-ui/tree/master/docs/data/joy/getting-started/templates/email"
                >
                    Sourcecode
                    <OpenInNewRoundedIcon/>
                </MenuItem>
                <ListDivider/>
                <MenuItem onClick={vm.logout}>
                    <LogoutRoundedIcon/>
                    Log out
                </MenuItem>
            </Menu>
        </Dropdown>
    )
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
                startDecorator={<AvatarButton vm={vm.avatar}/>}
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
