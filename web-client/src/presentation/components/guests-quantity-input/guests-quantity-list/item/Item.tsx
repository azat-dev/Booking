import React from "react";

import PropsItem from "./props";
import style from "./style.module.scss";
import useUpdatesFrom from "../../../../utils/binding/useUpdatesFrom";
import { ListItem, ListItemContent, Stack, Typography } from "@mui/joy";

import PlusIcon from "@mui/icons-material/Add";
import MinusIcon from "@mui/icons-material/Remove";
import Button from "./button";

const Item = ({ vm, title }: PropsItem) => {
    const [count] = useUpdatesFrom(vm.value);

    return (
        <ListItem>
            <ListItemContent>
                <Typography level="body-md">{title}</Typography>
            </ListItemContent>
            <Stack direction="row" alignItems="center" spacing={2}>
                <Button icon={<MinusIcon />} vm={vm.decrementButton} />
                <Typography level="body-sm">{count}</Typography>
                <Button icon={<PlusIcon />} vm={vm.incrementButton} />
            </Stack>
        </ListItem>
    );
};

export default React.memo(Item);
