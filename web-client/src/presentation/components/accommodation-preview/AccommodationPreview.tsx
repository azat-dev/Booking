import React from "react";

import AspectRatio from "@mui/joy/AspectRatio";
import Card from "@mui/joy/Card";
import CardCover from "@mui/joy/CardCover";
import Box from "@mui/joy/Box";
import Chip from "@mui/joy/Chip";
import IconButton from "@mui/joy/IconButton";
import Stack from "@mui/joy/Stack";
import Typography from "@mui/joy/Typography";
import Favorite from "@mui/icons-material/FavoriteBorderOutlined";
import StarIcon from "@mui/icons-material/Star";

import PropsAccommodationPreview from "./props";
import style from "./style.module.scss";
import { Link } from "react-router-dom";

const AccommodationPreview = ({ vm }: PropsAccommodationPreview) => {
    return (
        <Link to={vm.link} className={style.accommodationPreview}>
            <Card
                variant="plain"
                sx={{
                    width: "100%",
                    bgcolor: "initial",
                    p: 0,
                }}
            >
                <Box sx={{ position: "relative" }}>
                    <AspectRatio ratio="1/1">
                        <figure>
                            <img src={vm.image} loading="lazy" alt={vm.title} />
                        </figure>
                    </AspectRatio>
                </Box>
                <CardCover>
                    <div>
                        <Box
                            sx={{
                                p: 2,
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "space-between",
                                gap: 1.5,
                                flexGrow: 1,
                                alignSelf: "flex-start",
                            }}
                        >
                            <Chip variant="solid" color="primary">
                                Guests Choice
                            </Chip>
                            <IconButton
                                size="sm"
                                variant="plain"
                                color="neutral"
                                onClick={vm.toggleFavorite}
                            >
                                <Favorite />
                            </IconButton>
                        </Box>
                    </div>
                </CardCover>
                <Box
                    sx={{
                        display: "flex",
                        gap: 1,
                        alignItems: "center",
                        width: "100%",
                    }}
                >
                    <Stack
                        direction="column"
                        gap={0}
                        alignItems="stretch"
                        sx={{ width: "100%" }}
                    >
                        <Stack
                            direction="row"
                            gap={0.5}
                            justifyContent="space-between"
                        >
                            <Typography
                                sx={{
                                    fontSize: "md",
                                    fontWeight: "bold",
                                    flexGrow: 1,
                                }}
                            >
                                {vm.title}
                            </Typography>
                            {vm.rating && (
                                <Stack
                                    direction="row"
                                    gap={0.5}
                                    alignItems="center"
                                >
                                    <StarIcon sx={{ width: 15, height: 15 }} />
                                    <Typography
                                        noWrap
                                        sx={{
                                            fontSize: "sm",
                                            fontWeight: "md",
                                        }}
                                    >
                                        {vm.rating}
                                    </Typography>
                                </Stack>
                            )}
                        </Stack>
                        <Stack direction="row">
                            <Typography
                                sx={{ fontSize: "sm", fontWeight: "bold" }}
                            >
                                {vm.price}
                            </Typography>
                            <Typography
                                sx={{ fontSize: "sm", fontWeight: "sm" }}
                            >
                                &nbsp;
                                {vm.unit}
                            </Typography>
                        </Stack>
                    </Stack>
                </Box>
            </Card>
        </Link>
    );
};

export default React.memo(AccommodationPreview);
