import React from "react";

import PropsPageAccomodiationDetails from "./props";
import style from "./style.module.scss";
import { Avatar, Divider, Grid, Stack, Typography } from "@mui/joy";
import PhotosGroup from "./photos-group/PhotosGroup";
import { desktop, mobile, tablet } from "../../utils/selectors";
import NavigationBar from "../../components/navigation-bar/NavigationBar";

const PageAccomodiationDetails = ({ vm }: PropsPageAccomodiationDetails) => {
    return (
        <div className={style.pageAccomodiationDetails}>
            <NavigationBar vm={vm.navigationBar} />
            <Divider />
            <Stack direction="column" alignItems="center">
                <Stack
                    direction="column"
                    sx={(theme) => {
                        return {
                            [mobile(theme)]: {
                                width: "100%",
                            },
                            [tablet(theme)]: {
                                width: "100%",
                                maxWidth: 600,
                            },
                            [desktop(theme)]: {
                                width: "100%",
                                maxWidth: 1100,
                            },
                        };
                    }}
                >
                    <Typography level="h2">{vm.title}</Typography>
                    <br />
                    <PhotosGroup vm={vm.photosGroup} />
                    <Grid container sx={{ width: "100%" }}>
                        <Grid lg={8}>
                            <Stack direction="column">
                                <Typography level="h3">
                                    {vm.location}
                                </Typography>
                                <br />
                                <Divider />
                                <br />
                                <Stack
                                    direction="row"
                                    alignItems="center"
                                    spacing={1}
                                >
                                    <Avatar src={vm.hostPhoto} />
                                    <Typography
                                        level="body-md"
                                        fontWeight="bold"
                                    >
                                        Host: {vm.host}
                                    </Typography>
                                </Stack>
                                <br />
                                <Divider />
                                <br />
                                <Typography level="body-md">
                                    {vm.description}
                                </Typography>
                                <br />
                                <Divider />
                                <br />
                            </Stack>
                        </Grid>
                        <Grid lg={4}>
                            <Typography level="h1">{vm.location}</Typography>
                        </Grid>
                    </Grid>
                </Stack>
            </Stack>
        </div>
    );
};

export default React.memo(PageAccomodiationDetails);
