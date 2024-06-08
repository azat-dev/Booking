import * as React from 'react';
import {Box, Divider, IconButton, Stack} from '@mui/joy';
import Typography from '@mui/joy/Typography';

import MapsHomeWorkIcon from '@mui/icons-material/MapsHomeWork';

import PropsNavigationBar from './props';
import {desktop, mobile, tablet} from '../../utils/selectors';
import {Link} from 'react-router-dom';
import ProfileButton from './profile-button/ProfileButton';
import MuiLink from '@mui/joy/Link';
import Button from "@mui/joy/Button";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";

const NavigationBar = ({vm}: PropsNavigationBar) => {
    const [showHostingButton] = useUpdatesFrom(vm.showHostingButton);
    return (
        <>
            <Box
                sx={(theme) => ({
                    display: 'flex',
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    width: '100%',
                    boxSizing: 'border-box',
                    top: 0,
                    px: 1.5,
                    py: 1,
                    zIndex: 1000,
                    position: 'sticky',
                    [mobile(theme)]: {
                        backgroundColor: 'background.surface',
                    },
                    [tablet(theme)]: {
                        backgroundColor: 'background.surface',
                        paddingLeft: 5,
                        paddingRight: 5,
                    },
                    [desktop(theme)]: {
                        paddingLeft: 10,
                        paddingRight: 10,
                        paddingTop: 2,
                        backgroundColor: 'background.body',
                    },
                })}
            >
                <Link to='/' style={{color: 'inherit', textDecoration: 'none'}}>
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                            justifyContent: 'space-between',
                            gap: 1.5,
                        }}
                    >
                        <IconButton size='sm' variant='soft' color='primary'>
                            <MapsHomeWorkIcon/>
                        </IconButton>
                        <Typography component='h1' fontWeight='xl' color='primary'>
                            Demo Booking
                        </Typography>
                    </Box>
                </Link>

                <Stack direction='row' spacing={1}>
                    {
                        showHostingButton &&
                        <MuiLink
                            overlay
                            href='/listings'
                            underline='none'
                        >
                            <Button variant='plain' color='neutral'>
                                Switch to hosting
                            </Button>
                        </MuiLink>
                    }
                    <ProfileButton vm={vm.profileButton}/>
                </Stack>
            </Box>
            <Divider/>
        </>
    );
};

export default React.memo(NavigationBar);
