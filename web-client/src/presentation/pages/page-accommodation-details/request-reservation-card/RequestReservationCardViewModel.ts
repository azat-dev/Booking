import dayjs from "dayjs";
import DateRangePickerViewModel from "../../../components/date-picker/date-range-picker/DateRangePickerViewModel";
import {
    AvailableDates,
    CalendarRange,
} from "../../../components/date-picker/month-view/MonthViewModel";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";
import Cost from "../../../../domain/booking/values/Cost";
import DatesRange from "../../../../domain/booking/values/DatesRange";
import GuestsQuantity from "../../../../domain/booking/values/GuestsQuantity";
import AdultQuantity from "../../../../domain/booking/values/AdultQuantity";
import Date from "../../../../domain/booking/values/Date";
import GuestsQuantityInputViewModel from "../../../components/guests-quantity-input/GuestsQuantityInputViewModel";

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

class TestAvailableDates extends AvailableDates {
    isAvailable = (date: globalThis.Date): boolean => {
        return dayjs(date).isAfter(new globalThis.Date());
    };
}

export class LoadingButtonViewModel {
    public readonly isLoading: Subject<boolean>;
    public readonly text: Subject<string>;

    public constructor(
        initialIsLoading: boolean,
        initialText: string,
        private onClick: () => void
    ) {
        this.isLoading = value(initialIsLoading);
        this.text = value(initialText);
    }

    public click = () => {
        this.onClick();
    };

    public updateIsLoading = (isLoading: boolean) => {
        this.isLoading.set(isLoading);
    };

    public updateText = (text: string) => [this.text.set(text)];
}

class RequestReservationCardViewModel {
    public readonly costDetails: Subject<CostDetails>;
    public readonly dateRangePicker: DateRangePickerViewModel;
    private currentDateRange: CalendarRange | undefined;

    public readonly guestsQuantityInput: GuestsQuantityInputViewModel;

    public reservationButton: LoadingButtonViewModel;

    public constructor(
        private readonly getCostDetails: (
            dateRange: DatesRange,
            guests: GuestsQuantity
        ) => Promise<Cost>
    ) {
        this.guestsQuantityInput = new GuestsQuantityInputViewModel(
            new GuestsQuantity(new AdultQuantity(1)),
            () => {
                throw new Error("Not implemented");
            }
        );
        this.dateRangePicker = new DateRangePickerViewModel(
            this.currentDateRange,
            new TestAvailableDates(),
            this.didChangeDates
        );
        this.costDetails = value({ status: CostDetailsStatus.NOT_AVAILABLE });
        this.costDetails.set({
            status: CostDetailsStatus.LOADED,
            totalCost: "$100",
            accommodationCost: "$80",
            serviceFee: "$20",
        });

        this.reservationButton = new LoadingButtonViewModel(
            false,
            this.currentDateRange?.end ? "Reserve" : "Check Available Days",
            this.didClickReservationButton
        );
    }

    private didClickReservationButton = () => {
        if (!this.currentDateRange?.end) {
            this.dateRangePicker.open();
            return;
        }

        this.requestReservation();
    };

    private updateCostDetails = async () => {
        this.costDetails.set({ status: CostDetailsStatus.LOADING });

        const cost = await this.getCostDetails(
            new DatesRange(
                Date.fromDate(this.currentDateRange!.start),
                Date.fromDate(this.currentDateRange!.end!)
            ),
            new GuestsQuantity(new AdultQuantity(1))
        );

        this.costDetails.set({
            status: CostDetailsStatus.LOADED,
            totalCost: cost.totalCost.toString(),
            accommodationCost: cost.accommodationCost.toString(),
            serviceFee: cost.serviceFee.toString(),
        });
    };

    private didChangeDates = async (newRange: CalendarRange) => {
        this.currentDateRange = newRange;
        this.dateRangePicker.updateRange(newRange);

        this.reservationButton.updateText(
            newRange?.end ? "Reserve" : "Check Available Days"
        );

        if (newRange.end) {
            this.updateCostDetails();
            this.dateRangePicker.close();
        }
    };

    private requestReservation = (): void => {
        this.reservationButton.updateIsLoading(true);
        this.costDetails.set({ status: CostDetailsStatus.LOADING });

        setTimeout(() => {
            this.costDetails.set({
                status: CostDetailsStatus.LOADED,
                totalCost: "$100",
                accommodationCost: "$80",
                serviceFee: "$20",
            });
            this.reservationButton.updateIsLoading(false);
        }, 2000);
    };
}

export default RequestReservationCardViewModel;
