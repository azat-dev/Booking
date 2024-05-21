import React from "react";

import PropsPhotosGroup from "./props";
import AspectRatio from "@mui/joy/AspectRatio";
import {Card, CardCover} from "@mui/joy";

const PhotosGroup = ({vm}: PropsPhotosGroup) => {
    return (
        <AspectRatio ratio="2/1">
            <Card variant="plain">
                <CardCover>
                    <img
                        src={vm.mainPhoto.url}
                        loading="lazy"
                        style={{
                            objectFit: "cover",
                        }}
                    />
                </CardCover>
            </Card>
        </AspectRatio>
    );
};

export default React.memo(PhotosGroup);
