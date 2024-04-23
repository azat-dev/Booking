import React, { useRef, useEffect, useMemo } from "react";

import Box from "@mui/joy/Box";
import Button from "@mui/joy/Button";
import PropsPageMain from "./props";

import style from "./style.module.scss";
import NavigationBar from "../../components/navigation-bar/NavigationBar";
import useUpdatesFrom from "../../utils/binding/useUpdatesFrom";
import { ItemsViewModelState } from "./ItemsViewModel/ItemsViewModel";
import AccommodiationPreview from "../../components/accommodiation-preview/AccommodiationPreview";
import { Grid, Stack, Typography } from "@mui/joy";
import { desktop, mobile, tablet } from "../../utils/selectors";
import SearchInput from "../../components/search-input/SearchInput";
import AccommodiationPreviewLoading from "../../components/accommodiation-preview-loading/AccommodiationPreviewLoading";

const PageMain = ({ vm }: PropsPageMain) => {
    useEffect(() => {
        vm.load();
    }, []);

    const itemsViewModel = vm.itemsViewModel;
    const state = itemsViewModel.state;
    const updatedValues = useUpdatesFrom(state);
    const itemsState: ItemsViewModelState = updatedValues[0];

    return (
        <div className={style.pageMain}>
            <NavigationBar vm={vm.navigationBar} />
            {/* <Stack
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
            </Stack> */}
            <Box
                display="flex"
                flexDirection="column"
                alignItems="stretch"
                boxSizing="border-box"
                sx={(theme) => {
                    return {
                        [mobile(theme)]: {
                            padding: 3,
                        },
                        [tablet(theme)]: {
                            padding: 5,
                        },
                        [desktop(theme)]: {
                            padding: 5,
                        },
                    };
                }}
            >
                <Grid
                    container
                    columns={{
                        xs: 1,
                        sm: 2,
                        md: 4,
                        lg: 5,
                        xl: 6,
                    }}
                    boxSizing={"border-box"}
                    spacing={{
                        xs: 5,
                        sm: 3,
                        md: 3,
                        lg: 2,
                        xl: 3,
                    }}
                    sx={(theme) => {
                        return {
                            overflowX: "hidden",
                            [mobile(theme)]: {},
                            [tablet(theme)]: {},
                            [desktop(theme)]: {},
                        };
                    }}
                >
                    {itemsState.type === "showItems" &&
                        itemsState.items.map((item, index) => (
                            <Grid
                                key={item.id}
                                xs={1}
                                sm={1}
                                md={1}
                                lg={1}
                                xl={1}
                            >
                                {item.type === "loading" ? (
                                    <AccommodiationPreviewLoading />
                                ) : (
                                    <AccommodiationPreview vm={item.vm} />
                                )}
                            </Grid>
                        ))}
                </Grid>
            </Box>
            {itemsState.type === "showItems" && itemsState.showMoreButton && (
                <Box
                    sx={{
                        width: "100%",
                        boxSizing: "border-box",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        padding: 2,
                    }}
                >
                    <Button
                        onClick={itemsState.showMoreButton.click}
                        loadingPosition="start"
                        loading={itemsState.showMoreButton.isLoading}
                        size="lg"
                    >
                        Show More
                    </Button>
                </Box>
            )}
        </div>
    );
};

export default React.memo(PageMain);
