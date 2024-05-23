import React from "react";
import PropsUserPhoto from "./props";
import {Avatar, Skeleton, Stack} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import EditRoundedIcon from '@mui/icons-material/EditRounded';
import useScreenType, {ScreenType} from "../../../utils/hooks/useScreenType.ts";
import Button from "@mui/joy/Button";

const UserPhoto = ({vm}: PropsUserPhoto) => {
    const [isUploading, photo, initials] = useUpdatesFrom(vm.isUploading, vm.photo, vm.initials);

    const screenType = useScreenType();
    const size = screenType === ScreenType.MOBILE ? 120 : 240;

    return (
        <Stack direction="column" spacing={1} sx={{position: "relative", maxHeight: size}}>

            <Avatar
                src={isUploading ? '' : photo}
                sx={{minWidth: size, minHeight: size}}
                variant="outlined"
            >
                <Skeleton loading={isUploading}/>
                {initials}
            </Avatar>
            <Button
                startDecorator={
                    <EditRoundedIcon sx={{maxWidth: 20}}/>
                }
                onClick={vm.openUploadDialog}
                color="neutral"
                variant="outlined"
                size="sm"
                aria-label="upload new picture"
                disabled={isUploading}
                sx={{
                    bgcolor: 'background.body',
                    position: 'absolute',
                    zIndex: 100,
                    borderRadius: 20,
                    left: "50%",
                    bottom: 5,
                    boxShadow: 'sm',
                    transform: "translate(-50%, 50%)",
                }}
            >
                Edit
            </Button>
        </Stack>

    );
}

export default React.memo(UserPhoto);