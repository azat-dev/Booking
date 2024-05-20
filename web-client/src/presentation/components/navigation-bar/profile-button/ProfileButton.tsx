import React from "react";

import PropsProfileButton from "./props";
import style from "./style.module.scss";
import ProfileButtonAnonymous from "../profile-button-anonymous/ProfileButtonAnonymous";
import ProfileButtonAuthenticated from "../profile-button-authenticated/ProfileButtonAuthenticated";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import ProfileButtonAnonymousVM from "../profile-button-anonymous/ProfileButtonAnonymousVM";
import ProfileButtonAuthenticatedVM from "../profile-button-authenticated/ProfileButtonAuthenticatedVM";
import ProfileButtonLoadingVM from "../profile-button-loading/ProfileButtonLoadingVM";

const ProfileButton = ({vm}: PropsProfileButton) => {
    const [button] = useUpdatesFrom(vm);

    debugger
    switch (button.type) {
        case ProfileButtonAnonymousVM.TYPE:
            return (
                <ProfileButtonAnonymous vm={button}/>
            );

        case ProfileButtonAuthenticatedVM.TYPE:
            return (
                <ProfileButtonAuthenticated vm={button}/>
            );

        case ProfileButtonLoadingVM.TYPE:
            return (
                <div className={style.container}>
                    <div>Processing...</div>
                </div>
            );

        default:
            return null;
    }
};

export default React.memo(ProfileButton);
