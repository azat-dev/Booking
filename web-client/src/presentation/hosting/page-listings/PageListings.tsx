import React from "react";
import PropsPageListings from "./props";
import {Link, Typography} from "@mui/joy";
import Box from "@mui/joy/Box";
import Button from "@mui/joy/Button";
import AddIcon from '@mui/icons-material/AddRounded';
import ListingsTable from "./table/ListingsTable.tsx";
import HostingNavigationBar from "../../components/hosting-navigation-bar/HostingNavigationBar";

import style from "./style.module.scss";

const PageListings = ({vm}: PropsPageListings) => {

    return (
        <div className={style.pageListings}>
            <HostingNavigationBar vm={vm.navigationBar}/>
            <Box
                component="main"
                className="MainContent"
                sx={{
                    px: {xs: 2, md: 6},
                    pt: {
                        xs: 'calc(12px + var(--Header-height))',
                        sm: 'calc(12px + var(--Header-height))',
                        md: 3,
                    },
                    pb: {xs: 2, sm: 2, md: 3},
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    minWidth: 0,
                    height: '100dvh',
                    gap: 1,
                }}
            >
                <Box
                    sx={{
                        display: 'flex',
                        mb: 1,
                        gap: 1,
                        flexDirection: {xs: 'column', sm: 'row'},
                        alignItems: {xs: 'start', sm: 'center'},
                        flexWrap: 'wrap',
                        justifyContent: 'space-between',
                    }}
                >
                    <Typography level="h2" component="h1">
                        Your listings
                    </Typography>
                    <Link
                        overlay
                        underline='none'
                        href='/listings/add-new'
                    >
                        <Button
                            color="primary"
                            startDecorator={<AddIcon/>}
                            size="sm"
                        >
                            Add new listing
                        </Button>
                    </Link>

                    <ListingsTable vm={vm.listingsTable}/>
                </Box>

            </Box>
        </div>
    );
}

export default React.memo(PageListings);