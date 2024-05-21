import React from "react";
import PropsPageUserProfile from "./props";
import UserPhoto from "./user-photo/UserPhoto";

const PageUserProfile = ({vm}: PropsPageUserProfile) => {

    return (
        <div onClick={vm.updatePhoto}>

            <UserPhoto vm={vm.photo}/>
        </div>
    );
}

export default React.memo(PageUserProfile);