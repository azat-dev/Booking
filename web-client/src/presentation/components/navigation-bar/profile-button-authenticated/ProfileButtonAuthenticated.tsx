import React from "react";
import {Dropdown, ListDivider, Menu, MenuButton, MenuItem, Stack, Typography,} from "@mui/joy";
import PropsProfileButtonAuthenticated from "./props";
import AvatarButton from "./avatar-button/AvatarButton";
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import HelpRoundedIcon from '@mui/icons-material/HelpRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import Box from "@mui/joy/Box";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import useScreenType, {ScreenType} from "../../../utils/hooks/useScreenType";

const ProfileButtonAuthenticated = ({vm}: PropsProfileButtonAuthenticated) => {

    const [email, fullName] = useUpdatesFrom(vm.email, vm.fullName);
    const screenType = useScreenType();


    return (
        <Dropdown>
            <MenuButton
                variant={screenType === ScreenType.MOBILE ? "plain" : "outlined"}
                size="sm"
                sx={{
                    maxHeight: '48px',
                    borderRadius: '9999999px',
                    padding: screenType === ScreenType.MOBILE ? 0 : '10px',
                    paddingLeft: screenType === ScreenType.MOBILE ? 0 : '5px',
                }}
            >
                <Box
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        maxWidth: "100%",
                        minWidth: "100%",
                        overflow: "hidden"
                    }}
                >
                    <AvatarButton vm={vm.avatar}/>
                    {
                        [ScreenType.DESKTOP, ScreenType.TABLET].includes(screenType) &&
                        <Stack sx={{ml: 1.5}} direction="column" alignItems="flex-start">
                            <Typography level="title-sm" textColor="text.primary" noWrap maxWidth="9em">
                                {fullName}
                            </Typography>
                            <Typography level="body-xs" textColor="text.tertiary" noWrap maxWidth="9em">
                                {email}
                            </Typography>
                        </Stack>
                    }
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
                <MenuItem onClick={vm.openHelp}>
                    <HelpRoundedIcon/>
                    Help
                </MenuItem>
                <MenuItem onClick={vm.openProfile}>
                    <SettingsRoundedIcon/>
                    Profile
                </MenuItem>
                <ListDivider/>
                <MenuItem onClick={vm.logout}>
                    <LogoutRoundedIcon/>
                    Log out
                </MenuItem>
            </Menu>
        </Dropdown>
    );
};

export default React.memo(ProfileButtonAuthenticated);
