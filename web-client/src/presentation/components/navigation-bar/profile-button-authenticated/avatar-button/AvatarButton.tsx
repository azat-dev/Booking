import React from "react";

import PropsAvatarButton from "./props";
import style from "./style.module.scss";
import Avatar from "@mui/joy/Avatar";
import useUpdatesFrom from "../../../../utils/binding/useUpdatesFrom";

const AvatarButton = ({ vm }: PropsAvatarButton) => {
    const [fullName, shortName, photoUrl] = useUpdatesFrom(
        vm.fullName,
        vm.shortName,
        vm.photoUrl
    );
    return (
        <Avatar
            sx={{ maxWidth: "48px", maxHeight: "48px" }}
            alt={fullName}
            src={photoUrl}
            variant={photoUrl ? "outlined" : "soft"}
        >
            {shortName}
        </Avatar>
    );
};

export default React.memo(AvatarButton);
