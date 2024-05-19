import React from "react";
import PropsUserPhoto from "./props";
import {Avatar} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const UserPhoto = ({vm}: PropsUserPhoto) => {
    const [isUploading, photo, initials] = useUpdatesFrom(vm.isUploading, vm.photo, vm.initials);

    return (
        <Avatar src={photo}>
            {initials}
        </Avatar>
    );
}

export default React.memo(UserPhoto);