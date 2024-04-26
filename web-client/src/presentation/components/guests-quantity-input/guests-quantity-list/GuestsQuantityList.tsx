import React from "react";

import PropsGuestsQuantityList from "./props";
import style from "./style.module.scss";
import { List, ListDivider } from "@mui/joy";
import Item from "./item/Item";

const GuestsQuantityList = ({ vm }: PropsGuestsQuantityList) => {
    return (
        <List sx={{ "--List-gap": "20px" }}>
            <Item title="Adults" vm={vm.adultsItem} />
            <Item title="Children" vm={vm.childrenItem} />
        </List>
    );
};

export default React.memo(GuestsQuantityList);
