import React from "react";
import PropsPageUserProfile from "./props";
import UserPhoto from "./user-photo/UserPhoto";
import Box from "@mui/joy/Box";
import {List, ListDivider, Stack, Typography} from "@mui/joy";

import style from "./style.module.scss";
import {desktop, mobile, tablet} from "../../utils/selectors.ts";
import useScreenType, {ScreenType} from "../../utils/hooks/useScreenType.ts";
import PersonalInfoItem from "./personal-info-item/PersonalInfoItem.tsx";
import NavigationBar from "../../components/navigation-bar/NavigationBar.tsx";

const PageUserProfile = ({vm}: PropsPageUserProfile) => {

    const screenType = useScreenType();

    return (
        <Stack
            className={style.pageUserProfile}
            sx={{
                display: "flex",
                alignItems: 'center',
                justifyContent: 'center',
                width: "100%",
                height: "100%",
            }}
        >
            <NavigationBar vm={vm.navigationBar}/>

            <Box
                sx={theme => {
                    return {
                        [mobile(theme)]: {
                            width: "100%",
                            height: "100%",
                            boxSizing: 'border-box',
                            padding: 2
                        },
                        [tablet(theme)]: {
                            width: "100%",
                            height: "100%",
                            padding: 3,
                            boxSizing: 'border-box'
                        },
                        [desktop(theme)]: {
                            width: "100%",
                            maxWidth: 1200,
                            height: "100%",
                            padding: 4,
                            boxSizing: 'border-box'
                        }
                    };
                }}
            >
                <Stack direction={screenType === ScreenType.MOBILE ? "column" : "row"}>
                    <Box
                        sx={theme => {
                            return {
                                [mobile(theme)]: {
                                    width: "100%",
                                    padding: 2,
                                    paddingBottom: 5,
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    boxSizing: 'border-box'
                                },
                                [tablet(theme)]: {
                                    width: "100%",
                                    height: "100%",
                                    padding: 3,
                                    boxSizing: 'border-box'
                                },
                                [desktop(theme)]: {
                                    padding: 4,
                                    paddingRight: 10,
                                    width: "unset",
                                    height: "100%",
                                    boxSizing: 'border-box'
                                }
                            };
                        }}
                    >
                        <UserPhoto vm={vm.photo}/>
                    </Box>
                    <Stack direction="column" padding={2}>
                        <Typography level="h1">Your Profile</Typography>
                        <Typography level="body-lg" color="neutral">
                            Your information will be shared with other users in the app.
                        </Typography>
                        <br />
                        <List>
                            <PersonalInfoItem label="Name" vm={vm.name}/>
                            <ListDivider />
                            <PersonalInfoItem label="Email address" vm={vm.email}/>
                        </List>
                    </Stack>
                </Stack>
            </Box>

        </Stack>
    );
}

export default React.memo(PageUserProfile);