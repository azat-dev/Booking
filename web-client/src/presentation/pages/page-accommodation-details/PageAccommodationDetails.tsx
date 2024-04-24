import React from "react";

import PropsPageAccommodationDetails from "./props";
import style from "./style.module.scss";
import { Avatar, Box, Divider, Grid, Stack, Typography } from "@mui/joy";
import PhotosGroup from "./photos-group/PhotosGroup";
import { desktop, mobile, tablet } from "../../utils/selectors";
import NavigationBar from "../../components/navigation-bar/NavigationBar";

import StarIcon from "@mui/icons-material/Star";
import RequestReservationCard from "./request-reservation-card/RequestReservationCard";
import CalendarViewModel, {
    CalendarRange,
    MonthPosition,
} from "../../components/date-picker/calendar/CalendarViewModel";
import Calendar from "../../components/date-picker/calendar/Calendar";

const PageAccommodationDetails = ({ vm }: PropsPageAccommodationDetails) => {
    const calendar = new CalendarViewModel(
        new MonthPosition(4, 2024),
        new CalendarRange(new Date(2024, 3, 2), new Date())
    );
    return <Calendar vm={calendar} />;
    return (
        <div className={style.pageAccommodationDetails}>
            <NavigationBar vm={vm.navigationBar} />
            <Divider />
            <Stack
                direction="column"
                alignItems="center"
                sx={(theme) => {
                    return {
                        boxSizing: "border-box",
                        [mobile(theme)]: {
                            width: "100%",
                            padding: 3,
                        },
                        [tablet(theme)]: {
                            width: "100%",
                            padding: 3,
                        },
                        [desktop(theme)]: {
                            width: "100%",
                        },
                    };
                }}
            >
                <Stack
                    direction="column"
                    sx={(theme) => {
                        return {
                            [mobile(theme)]: {
                                width: "100%",
                            },
                            [tablet(theme)]: {
                                width: "100%",
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
                    <br />
                    <br />
                    <Grid container sx={{ width: "100%" }}>
                        <Grid lg={8}>
                            <Stack direction="column">
                                <Typography level="h3">
                                    {vm.location}
                                </Typography>
                                <Typography level="body-md" noWrap>
                                    {vm.roomInfo}
                                </Typography>
                                {vm.rating && (
                                    <Stack
                                        direction="row"
                                        gap={0.5}
                                        alignItems="center"
                                    >
                                        <StarIcon
                                            sx={{ width: 15, height: 15 }}
                                        />
                                        <Typography
                                            noWrap
                                            sx={{
                                                fontSize: "sm",
                                                fontWeight: "md",
                                            }}
                                        >
                                            {vm.rating}
                                        </Typography>
                                    </Stack>
                                )}
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
                            <Box
                                sx={{ paddingLeft: 5, boxSizing: "border-box" }}
                            >
                                <RequestReservationCard
                                    vm={vm.requestReservationCard}
                                />
                            </Box>
                        </Grid>
                    </Grid>
                </Stack>
            </Stack>
        </div>
    );
};

export default React.memo(PageAccommodationDetails);
