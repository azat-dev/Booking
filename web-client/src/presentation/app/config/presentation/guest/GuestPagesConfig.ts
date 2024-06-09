import GuestComponentsConfig from "./GuestComponentsConfig.ts";
import Accommodation from "../../../../../domain/accommodations/Accommodation.ts";
import Bus from "../../../../../domain/utils/Bus.ts";

class PagesConfig {
    public constructor(
        private readonly components: GuestComponentsConfig,
        private readonly bus: Bus
    ) {
    }

    public mainPage = async () => {
        const imp = import("../../../../pages/page-main/PageMainVMImpl.ts");
        const PageMainVMImpl = (await imp).default;

        return new PageMainVMImpl(
            this.components.navigationBar(),
            () => {
                console.log("Toggle favorite");
                throw new Error("Method not implemented.");
            }
        );
    }

    public accomodateDetailsPage = async (accomodation: Accommodation) => {
        const imp = import("../../../../pages/page-accommodation-details/PageAccommodationDetailsVM.ts");
        const PageAccommodationDetailsVM = (await imp).default;
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

export default PagesConfig;