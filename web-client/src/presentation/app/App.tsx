import React, { useMemo } from "react";

import PropsApp from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../utils/binding/useUpdatesFrom";
import LoginDialog from "../dialogs/login-dialog/LoginDialog";
import PageMain from "../pages/page-main/PageMain";
import PageMainViewModelImpl from "../pages/page-main/PageMainViewModelImpl";
import { ActiveDialogType } from "./AppViewModel";
import SignUpDialog from "../dialogs/sign-up-dialog/SignUpDialog";
import Accommodation, {
    Description,
    Owner,
    Photo,
    Title,
    Location,
    RoomInfo,
    AccommodationType,
} from "../../domain/accommodations/Accommodation";
import Avatar from "../../domain/auth/values/Avatar";
import PageAccommodationDetailsViewModel from "../pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import PageAccommodationDetails from "../pages/page-accommodation-details/PageAccommodationDetails";

const dialogTypes: any = {
    [ActiveDialogType.Login]: LoginDialog,
    [ActiveDialogType.SignUp]: SignUpDialog,
};

const App = ({ vm }: PropsApp) => {
    const [activeDialog] = useUpdatesFrom(vm.activeDialog);
    const pageMain = useMemo(
        () =>
            new PageMainViewModelImpl(
                vm.openLoginDialog,
                vm.openSignUpDialog,
                vm.toggleFavorite
            ),
        []
    );

    const accommodation = new Accommodation(
        new Title("Small House"),
        AccommodationType.House,
        new Location("USA", "New York"),
        new Description(
            "This is a small house in the middle of the city. It has a small garden and a garage. It is perfect for a small family. It is close to the city center. It has a small kitchen and a living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. It has a small living room. It has a small bathroom. It has a small bedroom. It has a small garden. It has a small garage. It has a small kitchen. "
        ),
        new Owner("John Doe", new Avatar("https://example.com/avatar.jpg")),
        [
            new Photo("https://picsum.photos/id/11/200/300"),
            new Photo("https://picsum.photos/id/12/200/300"),
            new Photo("https://picsum.photos/id/255/200/300"),
        ],
        2,
        [
            new RoomInfo(1, "Bedroom"),
            new RoomInfo(1, "Living room"),
            new RoomInfo(1, "Kitchen"),
            new RoomInfo(1, "Bathroom"),
        ],
        4.5
    );

    const pageAccommodationDetails = new PageAccommodationDetailsViewModel(
        accommodation,
        vm.openLoginDialog,
        vm.openSignUpDialog
    );

    const Dialog = dialogTypes[activeDialog?.type];

    return (
        <div className={style.app}>
            {/* <PageMain vm={pageMain} /> */}
            <PageAccommodationDetails vm={pageAccommodationDetails} />
            {activeDialog && <Dialog vm={activeDialog!.vm} />}
        </div>
    );
};

export default React.memo(App);
