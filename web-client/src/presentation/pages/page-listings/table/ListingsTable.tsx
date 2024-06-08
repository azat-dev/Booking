import React, {useEffect} from "react";
import PropsListingsTable from "./props";
import Table from "@mui/joy/Table";
import {Typography} from "@mui/joy";
import useUpdatesFrom from "../../../utils/binding/useUpdatesFrom.ts";
import Row from "./row/Row.tsx";
import RowVM from "./row/RowVM.ts";

const ListingsTable = ({vm}: PropsListingsTable) => {

    const [rows] = useUpdatesFrom(vm.rows);

    useEffect(() => {
        vm.load();
    }, []);

    return (
        <Table
            hoverRow
            size="sm"
            borderAxis="none"
            variant="soft"
            sx={{
                '--TableCell-paddingX': '1rem',
                '--TableCell-paddingY': '1rem',
            }}
        >
            <thead>
            <tr>
                <th>
                    <Typography level="title-sm">Listing</Typography>
                </th>
                <th>
                    <Typography level="title-sm">Location</Typography>
                </th>
                <th>
                    <Typography level="title-sm">Status</Typography>
                </th>
            </tr>
            </thead>
            <tbody>
                {rows.map((row: RowVM) => <Row key={row.id} vm={row}/>)}
            </tbody>
        </Table>
    );
}

export default React.memo(ListingsTable);