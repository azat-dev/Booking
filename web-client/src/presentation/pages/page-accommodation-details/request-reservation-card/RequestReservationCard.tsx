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
    Stack,
    Typography,
} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom";
import { CostDetailsStatus } from "./RequestReservationCardViewModel";
import DateRangePicker from "../../../components/date-picker/date-range-picker/DateRangePicker";

const RequestReservationCard = ({ vm }: PropsRequestReservationCard) => {
    const [costDetails] = useUpdatesFrom(vm.costDetails);
    return (
        <Card variant="outlined" sx={{ boxSizing: "border-box" }}>
            <DateRangePicker vm={vm.dateRangePicker} />

            <Button size="lg" color="primary" onClick={vm.requestReservation}>
                Reserve
            </Button>
            {costDetails && costDetails.status === CostDetailsStatus.LOADED && (
                <List>
                    <ListItem>
                        <ListItemContent>
                            <Typography fontSize="sm">Accommodation</Typography>
                        </ListItemContent>
                        <Typography>{costDetails.accommodationCost}</Typography>
                    </ListItem>

                    <ListItem>
                        <ListItemContent>
                            <Typography fontSize="sm">Service fee</Typography>
                        </ListItemContent>
                        <Typography>{costDetails.serviceFee}</Typography>
                    </ListItem>

                    <ListDivider />

                    <ListItem>
                        <ListItemContent>
                            <Typography fontWeight="bold">Total</Typography>
                        </ListItemContent>
                        <Typography fontWeight="bold">
                            {costDetails.totalCost}
                        </Typography>
                    </ListItem>
                </List>
            )}
        </Card>
    );
};

export default React.memo(RequestReservationCard);
