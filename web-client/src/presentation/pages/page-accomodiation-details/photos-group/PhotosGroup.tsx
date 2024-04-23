import React from "react";

import PropsPhotosGroup from "./props";
import style from "./style.module.scss";
import Box from "@mui/joy/Box";
import AspectRatio from "@mui/joy/AspectRatio";
import { Card, CardContent, CardCover, Grid } from "@mui/joy";

const PhotosGroup = ({ vm }: PropsPhotosGroup) => {
    return (
        <Grid
            container
            spacing={1}
            columns={2}
            sx={{ backgroundColor: "background.body" }}
        >
            <Grid sm={1} sx={{ backgroundColor: "background.body" }}>
                <AspectRatio
                    ratio="1/1"
                    sx={{ backgroundColor: "background.body" }}
                >
                    <img
                        src={vm.mainPhoto.url}
                        loading="lazy"
                        style={{
                            borderTopLeftRadius: "10px",
                            borderBottomLeftRadius: "10px",
                        }}
                    />
                </AspectRatio>
            </Grid>
            {vm.otherPhotos && (
                <Grid sm={1} spacing={0}>
                    <Grid container columns={2} spacing={1}>
                        {vm.otherPhotos.items.map((photo, index) => (
                            <Grid key={index} sm={2}>
                                <AspectRatio ratio="2/1">
                                    <img src={photo.url} loading="lazy" />
                                </AspectRatio>
                            </Grid>
                        ))}
                    </Grid>
                </Grid>
            )}
        </Grid>
    );
};

export default React.memo(PhotosGroup);
