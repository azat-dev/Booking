import React from "react";
import PropsTabs from "./props";
import {Tab, tabClasses, TabList, Tabs as JoyTabs} from "@mui/joy";
import Box from "@mui/joy/Box";

const Tabs = ({vm}: PropsTabs) => {

    const index = 0;

    return (
        <Box
            sx={{
                flexGrow: 1,
                m: -2,
                overflowX: 'hidden',
                borderBottom: 'none'
            }}
        >
            <JoyTabs
                aria-label="Pipeline"
                value={index}
                onChange={(event, value) => setIndex(value as number)}
            >
                <TabList
                    sx={{
                        pt: 1,
                        justifyContent: 'center',
                        [`&& .${tabClasses.root}`]: {
                            flex: 'initial',
                            bgcolor: 'transparent',
                            '&:hover': {
                                bgcolor: 'transparent',
                            },
                            [`&.${tabClasses.selected}`]: {
                                color: 'primary.plainColor',
                                '&::after': {
                                    height: 2,
                                    borderTopLeftRadius: 3,
                                    borderTopRightRadius: 3,
                                    bgcolor: 'primary.500',
                                },
                            },
                        }
                    }}
                >
                    <Tab indicatorInset>
                        Listings
                    </Tab>
                    <Tab indicatorInset>
                        Calendar
                    </Tab>
                    <Tab indicatorInset>
                        Inbox
                    </Tab>
                </TabList>
            </JoyTabs>
        </Box>
    );
}

export default React.memo(Tabs);