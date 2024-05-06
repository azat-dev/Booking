import * as React from "react";
import { Box, IconButton, Menu, MenuButton, MenuItem, Theme } from "@mui/joy";
import Typography from "@mui/joy/Typography";

import MapsHomeWorkIcon from "@mui/icons-material/MapsHomeWork";

import PropsNavigationBar from "./props";

import style from "./style.module.scss";
import { mobile, tablet, desktop } from "../../utils/selectors";
import { Link } from "react-router-dom";
import ProfileButtonAnonymous from "./profile-button-anonymous/ProfileButtonAnonymous";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";

const NavigationBar = ({ vm }: PropsNavigationBar) => {
    const [profileButton] = useUpdatesFrom(vm.profileButton);
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
            <Link to="/" style={{ color: "inherit", textDecoration: "none" }}>
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
            </Link>

            {profileButton?.type === "anonymous" && (
                <ProfileButtonAnonymous vm={profileButton.vm} />
            )}
        </Box>
    );
};

export default React.memo(NavigationBar);
