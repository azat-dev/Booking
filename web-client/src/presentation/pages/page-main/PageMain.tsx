import React, { useRef, useEffect, useMemo } from "react";

import Box from "@mui/joy/Box";
import Stack from "@mui/joy/Stack";
import PropsPageMain from "./props";

import style from "./style.module.scss";
import NavigationBar from "../../components/navigation-bar/NavigationBar";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import { ItemsViewModelState } from "./ItemsViewModel/ItemsViewModel";
import AccommodiationPreview from "../../components/accommodiation-preview/AccommodiationPreview";
import Typography from "@mui/joy/Typography";
import SearchInput from "../../components/search-input/SearchInput";

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
            <Stack
                sx={{
                    px: { xs: 2, md: 4 },
                    py: 2,
                    borderBottom: "1px solid",
                    borderColor: "divider",
                }}
            >
                <Stack sx={{ mb: 2, alignItems: "center" }}>
                    <Typography level="h2">Start your jorney here!</Typography>
                    <Typography level="body-md" color="neutral">
                        Find the best place to stay
                    </Typography>
                </Stack>
                <Stack sx={{ alignItems: "center" }}>
                    <Box sx={{ maxWidth: 900, minWidth: 900 }}>
                        <SearchInput vm={vm.searchInput} />
                    </Box>
                </Stack>
            </Stack>
            {itemsState.type === "loading" && <div>Loading...</div>}
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns:
                        "repeat(auto-fit, minmax(240px, 300px))",
                    gap: 5,
                    padding: 5,
                    paddingLeft: 10,
                    paddingRight: 10,
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
