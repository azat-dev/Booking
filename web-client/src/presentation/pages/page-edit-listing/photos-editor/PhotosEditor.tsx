import React from "react";
import PropsPhotosEditor from "./props";
import {Typography} from "@mui/joy";
import Box from "@mui/joy/Box";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom.ts";
import {desktop, mobile, tablet} from "../../../utils/selectors.ts";
import InitialPhotoInput from "./initial-photo-input/InitialPhotoInput.tsx";


const PhotosEditor = ({vm}: PropsPhotosEditor) => {

    const [initialPhotoInput] = useUpdatesFrom(vm.initialPhotoInput);

    return (
        <Box
            sx={(theme) => ({
                [mobile(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'flex-start',
                    gap: '10px',
                    padding: 3
                },
                [desktop(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px'
                },
                [tablet(theme)]: {
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px'
                },
            })}
        >

            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    maxWidth: 500
                }}
            >
                <Typography level='h1'>
                    Add at least 5 photos
                </Typography>
                <Typography>
                    Showcase your space, highlight the experience, and inspire guests
                </Typography>

                <br/>

                {
                    initialPhotoInput &&
                    <InitialPhotoInput vm={initialPhotoInput}/>
                }
            </Box>
        </Box>
    );
}

export default React.memo(PhotosEditor);