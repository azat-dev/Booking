import React from "react";
import PropsPageUserProfile from "./props";
import UserPhoto from "./user-photo/UserPhoto";
import Box from "@mui/joy/Box";
import {Stack, Typography} from "@mui/joy";

import style from "./style.module.scss";

const PageUserProfile = ({vm}: PropsPageUserProfile) => {

    return (
        <Box
             className={style.pageUserProfile}
             display="flex"
             align-items="center"
             justify-content="center"
             width="100%"
             height="100%"
        >

            <Stack direction="row">
                <UserPhoto vm={vm.photo}/>
                <Stack direction="column">
                    <Typography level="h1">Your Profile</Typography>
                </Stack>
            </Stack>

        </Box>
    );
}

export default React.memo(PageUserProfile);