import { Photo } from "../../../../domain/accommodations/Accommodation";

export interface ShowAllImagesButtonVM {
    click: () => void;
}

export interface PhotoVM {
    url: string;
    numberOfColumns: number;
    aspectRatio: string;
}

export interface OtherPhotosData {
    numberOfColumns: number;
    items: PhotoVM[];
}

class PhotosGroupVM {
    public readonly mainPhoto: PhotoVM;
    public readonly otherPhotos: OtherPhotosData | undefined;
    public showAllImagesButton: ShowAllImagesButtonVM | undefined;

    public constructor(public readonly photos: Photo[]) {
        this.mainPhoto = {
            numberOfColumns: 12,
            url: photos[0].url,
            aspectRatio: "1/1",
        };

        if (photos.length > 5) {
            this.showAllImagesButton = {
                click: () => {
                    console.log("Show all images");
                },
            };
        }

        // if (photos.length == 2) {
        this.otherPhotos = {
            numberOfColumns: 1,
            items: [
                {
                    numberOfColumns: 1,
                    url: photos[1].url,
                    aspectRatio: "1/1",
                },
            ],
        };
        // } else if (photos.length == 3) {
        //     this.otherPhotos = {
        //         numberOfColumns: 2,
        //         items: [
        //             {
        //                 numberOfColumns: 2,
        //                 url: photos[1].url,
        //                 aspectRatio: "2/1",
        //             },
        //             {
        //                 numberOfColumns: 2,
        //                 url: photos[2].url,
        //                 aspectRatio: "2/1",
        //             },
        //         ],
        //     };
        // } else if (photos.length >= 5) {
        //     this.otherPhotos = {
        //         numberOfColumns: 2,
        //         items: [
        //             {
        //                 numberOfColumns: 1,
        //                 url: photos[1].url,
        //                 aspectRatio: "1/1",
        //             },
        //             {
        //                 numberOfColumns: 1,
        //                 url: photos[2].url,
        //                 aspectRatio: "1/1",
        //             },
        //             {
        //                 numberOfColumns: 1,
        //                 url: photos[3].url,
        //                 aspectRatio: "1/1",
        //             },
        //             {
        //                 numberOfColumns: 1,
        //                 url: photos[4].url,
        //                 aspectRatio: "1/1",
        //             },
        //         ],
        //     };
        // }
    }
}

export default PhotosGroupVM;
