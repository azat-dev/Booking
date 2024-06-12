import React from "react";
import PropsRow from "./props.ts";
import {Typography} from "@mui/joy";

const Row = ({vm}: PropsRow) => {

    return (
        <tr onClick={(e) => {
            alert("OPE")
            e.stopPropagation();
            vm.click();
        }}>
            <td>
                <Typography
                    level="title-sm"
                    sx={{alignItems: 'flex-start'}}
                >
                    {vm.title}111
                </Typography>
            </td>
            <td>
                <Typography level="body-sm">
                    {vm.location}
                </Typography>
            </td>
            <td>
                <Typography level="body-sm">
                    {vm.status}
                </Typography>
            </td>
        </tr>
    );
}

export default React.memo(Row);