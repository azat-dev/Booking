import React from "react";

import PropsRequestReservationCard from "./props";
import style from "./style.module.scss";
import {
    Button,
    Card,
    Divider,
    List,
    ListDivider,
    ListItem,
    ListItemContent,
    Skeleton,
    Stack,
    Typography,
} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import { CostDetailsStatus } from "./RequestReservationCardVM";
import DateRangePicker from "../../../components/date-picker/date-range-picker/DateRangePicker";
import GuestsQuantityInput from "../../../components/guests-quantity-input/GuestsQuantityInput";

const RequestReservationCard = ({ vm }: PropsRequestReservationCard) => {
    const [costDetails, isReservationButtonLoading, reservationButtonText] =
        useUpdatesFrom(
            vm.costDetails,
            vm.reservationButton.isLoading,
            vm.reservationButton.text
        );

    return (
        <Card variant="outlined" sx={{ boxSizing: "border-box" }}>
            <DateRangePicker vm={vm.dateRangePicker} />
            <GuestsQuantityInput vm={vm.guestsQuantityInput} />

            <Button
                size="lg"
                color="primary"
                onClick={vm.reservationButton.click}
                loadingPosition="start"
                loading={isReservationButtonLoading}
            >
                {reservationButtonText}
            </Button>
            {costDetails &&
                [CostDetailsStatus.LOADED, CostDetailsStatus.LOADING].includes(
                    costDetails.status
                ) && (
                    <List>
                        <ListItem>
                            <ListItemContent>
                                <Typography fontSize="sm">
                                    <Skeleton
                                        loading={
                                            costDetails.status ===
                                            CostDetailsStatus.LOADING
                                        }
                                    >
                                        Accommodation
                                    </Skeleton>
                                </Typography>
                            </ListItemContent>
                            <Typography>
                                <Skeleton
                                    loading={
                                        costDetails.status ===
                                        CostDetailsStatus.LOADING
                                    }
                                >
                                    {costDetails.accommodationCost ?? "0000"}
                                </Skeleton>
                            </Typography>
                        </ListItem>

                        <ListItem>
                            <ListItemContent>
                                <Typography fontSize="sm">
                                    <Skeleton
                                        loading={
                                            costDetails.status ===
                                            CostDetailsStatus.LOADING
                                        }
                                    >
                                        Service fee
                                    </Skeleton>
                                </Typography>
                            </ListItemContent>
                            <Typography>
                                <Skeleton
                                    loading={
                                        costDetails.status ===
                                        CostDetailsStatus.LOADING
                                    }
                                >
                                    {costDetails.serviceFee ?? "0000"}
                                </Skeleton>
                            </Typography>
                        </ListItem>

                        <ListDivider />

                        <ListItem>
                            <ListItemContent>
                                <Typography fontWeight="bold">
                                    <Skeleton
                                        loading={
                                            costDetails.status ===
                                            CostDetailsStatus.LOADING
                                        }
                                    >
                                        Total
                                    </Skeleton>
                                </Typography>
                            </ListItemContent>
                            <Typography fontWeight="bold">
                                <Skeleton
                                    loading={
                                        costDetails.status ===
                                        CostDetailsStatus.LOADING
                                    }
                                >
                                    {costDetails.totalCost ?? "000"}
                                </Skeleton>
                            </Typography>
                        </ListItem>
                    </List>
                )}
        </Card>
    );
};

export default React.memo(RequestReservationCard);
