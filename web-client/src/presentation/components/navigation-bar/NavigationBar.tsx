import * as React from "react";
import { Box, IconButton } from "@mui/joy";
import Typography from "@mui/joy/Typography";
import Avatar from "@mui/joy/Avatar";
import MapsHomeWorkIcon from "@mui/icons-material/MapsHomeWork";

import PropsNavigationBar from "./props";

import style from "./style.module.scss";

const NavigationBar = ({ vm }: PropsNavigationBar) => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "row",
                justifyContent: "space-between",
                alignItems: "center",
                width: "100%",
                top: 0,
                px: 1.5,
                py: 1,
                zIndex: 10000,
                position: "sticky",
                paddingLeft: 10,
                paddingTop: 2,
            }}
        >
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
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

            <Box sx={{ display: "flex", flexDirection: "row", gap: 3 }}>
                <Box
                    sx={{
                        gap: 1,
                        alignItems: "center",
                        display: { xs: "none", sm: "flex" },
                    }}
                ></Box>
            </Box>
        </Box>
    );
};

export default React.memo(NavigationBar);
