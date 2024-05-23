import React from "react";
import PropsPersonalInfoItem from "./props";
import {ListItem, Stack, Typography} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom.ts";

const PersonalInfoItem = ({vm, label}: PropsPersonalInfoItem) => {
    const [text] = useUpdatesFrom(vm.text);

    return (
        <ListItem>
            <Stack sx={{paddingBottom: 1}}>
                <Typography level="body-md">{label}: </Typography>
                <Typography level="body-md" color="neutral">{text}</Typography>
            </Stack>
        </ListItem>
    );
}

export default React.memo(PersonalInfoItem);