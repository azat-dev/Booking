import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";

export enum CostDetailsStatus {
    LOADING = "loading",
    LOADED = "loaded",
    NOT_AVAILABLE = "not-available",
}

export type CostDetails =
    | {
          status: CostDetailsStatus.LOADING;
      }
    | {
          status: CostDetailsStatus.LOADED;
          totalCost: string;
          accommodationCost: string;
          serviceFee: string;
      }
    | {
          status: CostDetailsStatus.NOT_AVAILABLE;
      };

class RequestReservationCardViewModel {
    public readonly costDetails: Subject<CostDetails>;

    public constructor() {
        this.costDetails = value({ status: CostDetailsStatus.NOT_AVAILABLE });
        this.costDetails.set({
            status: CostDetailsStatus.LOADED,
            totalCost: "$100",
            accommodationCost: "$80",
            serviceFee: "$20",
        });
    }

    public requestReservation = (): void => {
        this.costDetails.set({ status: CostDetailsStatus.LOADING });

        setTimeout(() => {
            this.costDetails.set({
                status: CostDetailsStatus.LOADED,
                totalCost: "$100",
                accommodationCost: "$80",
                serviceFee: "$20",
            });
        }, 2000);
    };
}

export default RequestReservationCardViewModel;
