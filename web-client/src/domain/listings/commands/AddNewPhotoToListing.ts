import type {Emit} from "../../utils/Bus";
import FailedToAddNewPhotoToListing from "../events/FailedToAddNewPhotoToListing";
import {CommandsListingsPhotoApi} from "../../../data/api/listings";
import ProcessingAddNewPhotoToListing from "../events/ProcessingAddNewPhotoToListing.ts";
import uploadPhoto from "../../../data/uploadPhoto.ts";
import NewPhotoAddedToListing from "../events/NewPhotoAddedToListing.ts";
import ListingId from "../values/ListingId.ts";

class AddNewPhotoToListing {

    public constructor(
        private readonly photoApi: CommandsListingsPhotoApi,
        private readonly emit: Emit,
    ) {
    }

    public execute = async (listingId: ListingId, file: File): Promise<void> => {

        try {
            this.emit(new ProcessingAddNewPhotoToListing(listingId));

            const {fileName, fileExtension} = this.splitFilename(file.name);

            const response = await this.photoApi.generateUploadListingPhotoUrl({
                listingId: listingId.val,
                generateUploadListingPhotoUrlRequestBody: {
                    operationId: crypto.randomUUID(),
                    fileName,
                    fileExtension,
                    fileSize: file.size,
                }
            });

            const isPhotoUploaded = await uploadPhoto(
                response.objectPath.url,
                response.formData,
                file
            );


            await this.photoApi.addPhotoToListing({
                listingId: listingId.val,
                addListingPhotoRequestBody: {
                    operationId: crypto.randomUUID(),
                    uploadedFile: response.objectPath
                }
            })

            this.emit(new NewPhotoAddedToListing(listingId));
        } catch (error) {
            const errorMessage = new FailedToAddNewPhotoToListing(listingId, error);
            this.emit(errorMessage);
            throw errorMessage;
        }
    }

    private splitFilename = (fileName: string) => {
        const parts = fileName.split('.');
        const fileExtension = parts[parts.length - 1];
        const name = parts.slice(0, parts.length - 1).join('.');
        return {fileName: name, fileExtension};
    }
}

export default AddNewPhotoToListing;