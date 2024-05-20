import PageMainVMImpl from "./presentation/pages/page-main/PageMainVMImpl";
import ComponentsModule from "./presentation/app/app-model/ComponentsModule";
import PageAccommodationDetailsVM
    from "./presentation/pages/page-accommodation-details/PageAccommodationDetailsVM";
import Accommodation from "./domain/accommodations/Accommodation";

class PagesModule {

    public constructor(
        private readonly components: ComponentsModule
    ) {
    }

    public mainPage = () => {
        return new PageMainVMImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = (accomodation: Accommodation) => {
        return new PageAccommodationDetailsVM(
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