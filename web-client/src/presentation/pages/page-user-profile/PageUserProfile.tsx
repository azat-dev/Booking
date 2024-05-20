import React from "react";
import PropsPageUserProfile from "./props";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import UserPhoto from "./user-photo/UserPhoto";

const PageUserProfile = ({vm}: PropsPageUserProfile) => {

    // const [pho] = useUpdatesFrom(vm.photo);

    return (
        <div>

            <UserPhoto vm={vm.photo} />
        </div>
    );
}

export default React.memo(PageUserProfile);