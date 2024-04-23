import React from "react";

import AspectRatio from "@mui/joy/AspectRatio";
import Card from "@mui/joy/Card";
import Box from "@mui/joy/Box";
import Stack from "@mui/joy/Stack";
import Typography from "@mui/joy/Typography";

import PropsAccommodationPreviewLoading from "./props";
import Skeleton from "@mui/joy/Skeleton";

import style from "./style.module.scss";

const AccommodationPreviewLoading = (
    props: PropsAccommodationPreviewLoading
) => {
    return (
        <Card
            variant="plain"
            sx={{
                width: "100%",
                bgcolor: "initial",
                p: 0,
            }}
        >
            <Box sx={{ position: "relative" }}>
                <AspectRatio ratio="4/4">
                    <Skeleton animation="wave" variant="overlay">
                        <img
                            alt=""
                            src="data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs="
                        />
                    </Skeleton>
                </AspectRatio>
            </Box>

            <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
                <Stack direction="column" gap={0.5}>
                    <Stack direction="row" gap={0.5}>
                        <Typography sx={{ fontSize: "sm", fontWeight: "md" }}>
                            <Skeleton animation="wave">
                                Title placeholder some long tex
                            </Skeleton>
                        </Typography>
                        <Typography sx={{ fontSize: "sm", fontWeight: "md" }}>
                            <Skeleton animation="wave">Rating</Skeleton>
                        </Typography>
                    </Stack>
                    <Typography sx={{ fontSize: "sm", fontWeight: "md" }}>
                        <Skeleton animation="wave">Lorem ipsum i</Skeleton>
                    </Typography>
                </Stack>
            </Box>
        </Card>
    );
};

export default React.memo(AccommodationPreviewLoading);
