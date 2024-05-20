import Bus from "./domain/utils/Bus";
import DialogsStore from "./presentation/stores/DialogsStore";
import DialogsModule from "./presentation/app/app-model/DialogsModule";
import PageMainViewModelImpl from "./presentation/pages/page-main/PageMainViewModelImpl";
import ComponentsModule from "./presentation/app/app-model/ComponentsModule";
import PageAccommodationDetailsViewModel
    from "./presentation/pages/page-accommodation-details/PageAccommodationDetailsViewModel";
import Accommodation from "./domain/accommodations/Accommodation";

class PagesModule {

    public constructor(
        private readonly components: ComponentsModule
    ) {
    }

    public mainPage = () => {
        return new PageMainViewModelImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = (accomodation: Accommodation) => {
        return new PageAccommodationDetailsViewModel(
            accomodation,
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        )
    }
}

export default PagesModule;