import React from "react";

import AspectRatio from "@mui/joy/AspectRatio";
import Card from "@mui/joy/Card";
import CardContent from "@mui/joy/CardContent";
import CardOverflow from "@mui/joy/CardOverflow";
import CardCover from "@mui/joy/CardCover";
import Box from "@mui/joy/Box";
import Chip from "@mui/joy/Chip";
import IconButton from "@mui/joy/IconButton";
import Link from "@mui/joy/Link";
import Stack from "@mui/joy/Stack";
import Typography from "@mui/joy/Typography";
import WorkspacePremiumRoundedIcon from "@mui/icons-material/WorkspacePremiumRounded";
import FavoriteRoundedIcon from "@mui/icons-material/FavoriteRounded";
import FmdGoodRoundedIcon from "@mui/icons-material/FmdGoodRounded";
import KingBedRoundedIcon from "@mui/icons-material/KingBedRounded";
import WifiRoundedIcon from "@mui/icons-material/WifiRounded";
import Star from "@mui/icons-material/Star";
import Favorite from "@mui/icons-material/FavoriteBorderOutlined";

import PropsAccommodiationPreview from "./props";
import style from "./style.module.scss";

const AccommodiationPreview = ({ vm }: PropsAccommodiationPreview) => {
    return (
        <div className={style.accommodiationPreview}>
            <Card
                variant="plain"
                sx={{
                    width: 300,
                    bgcolor: "initial",
                    p: 0,
                }}
            >
                <Box sx={{ position: "relative" }}>
                    <AspectRatio ratio="4/4">
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
                                // sx={{ bgcolor: "rgba(0 0 0 / 0.2)" }}
                            >
                                <Favorite onClick={vm.toggleFavorite} />
                            </IconButton>
                        </Box>
                    </div>
                </CardCover>
                <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
                    <Stack direction="column" gap={0.5}>
                        <Stack direction="row" gap={0.5}>
                            <Typography
                                sx={{ fontSize: "sm", fontWeight: "md" }}
                            >
                                {vm.title}
                            </Typography>
                            <Typography
                                sx={{ fontSize: "sm", fontWeight: "md" }}
                            >
                                {vm.rating}
                            </Typography>
                        </Stack>
                        <Typography sx={{ fontSize: "sm", fontWeight: "md" }}>
                            {vm.price}
                        </Typography>
                    </Stack>
                </Box>
            </Card>
        </div>
    );
};

export default React.memo(AccommodiationPreview);
