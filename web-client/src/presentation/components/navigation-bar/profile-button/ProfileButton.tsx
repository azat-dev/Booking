import React from "react";

import PropsProfileButton from "./props";
import style from "./style.module.scss";
import {ProfileButtonPresentationType} from "./ProfileButtonVM";
import ProfileButtonAnonymous from "../profile-button-anonymous/ProfileButtonAnonymous";
import ProfileButtonAuthenticated from "../profile-button-authenticated/ProfileButtonAuthenticated";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";

const ProfileButton = ({vm}: PropsProfileButton) => {
    const [button] = useUpdatesFrom(vm.button);

    switch (button.type) {
        case ProfileButtonPresentationType.ANONYMOUS:
            return (
                <ProfileButtonAnonymous vm={button.vm}/>
            );

        case ProfileButtonPresentationType.AUTHENTICATED:
            return (
                <ProfileButtonAuthenticated vm={button.vm}/>
            );

        case ProfileButtonPresentationType.PROCESSING:
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
