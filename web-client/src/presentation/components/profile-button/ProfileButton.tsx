import React from "react";

import PropsProfileButton from "./props.ts";
import style from "./style.module.scss";
import ProfileButtonAnonymous from "./profile-button-anonymous/ProfileButtonAnonymous.tsx";
import ProfileButtonAuthenticated from "./profile-button-authenticated/ProfileButtonAuthenticated.tsx";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";
import ProfileButtonAnonymousVM from "./profile-button-anonymous/ProfileButtonAnonymousVM.ts";
import ProfileButtonAuthenticatedVM from "./profile-button-authenticated/ProfileButtonAuthenticatedVM.ts";
import ProfileButtonLoadingVM from "./profile-button-loading/ProfileButtonLoadingVM.ts";

const ProfileButton = ({vm}: PropsProfileButton) => {
    const [button] = useUpdatesFrom(vm);


    switch (button.type) {
        case ProfileButtonAnonymousVM.type:
            return (
                <ProfileButtonAnonymous vm={button}/>
            );

        case ProfileButtonAuthenticatedVM.type:
            return (
                <ProfileButtonAuthenticated vm={button}/>
            );

        case ProfileButtonLoadingVM.type:
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
