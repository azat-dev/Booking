import React, { useRef, useEffect, useMemo } from "react";

import Box from "@mui/joy/Box";
import PropsPageMain from "./props";

import style from "./style.module.scss";
import PageMainViewModelImpl from "./PageMainViewModelImpl";
import NavigationBar from "../../components/navigation-bar/NavigationBar";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import { ItemsViewModelState } from "./ItemsViewModel/ItemsViewModel";
import AccommodiationPreview from "../../components/accommodiation-preview/AccommodiationPreview";

const PageMain = ({ vm }: PropsPageMain) => {
    useEffect(() => {
        console.log("LOAD");
        vm.load();
    }, []);

    const itemsViewModel = vm.itemsViewModel;
    const state = itemsViewModel.state;
    const updatedValues = useUpdatesFrom(state);
    const itemsState: ItemsViewModelState = updatedValues[0];

    return (
        <div className={style.pageMain}>
            <NavigationBar vm={vm.navigationBar} />
            {itemsState.type === "loading" && <div>Loading...</div>}
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns:
                        "repeat(auto-fit, minmax(240px, 300px))",
                    gap: 5,
                }}
            >
                {itemsState.type === "loaded" &&
                    itemsState.items.map((item, index) => (
                        <AccommodiationPreview key={item.id} vm={item} />
                    ))}
            </Box>
        </div>
    );
};

export default React.memo(PageMain);
