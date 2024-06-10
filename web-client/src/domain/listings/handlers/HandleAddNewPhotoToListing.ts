import type Bus from "../../utils/Bus";
import Handler from "../../utils/Handler";
import AddNewPhotoToListing from "../commands/AddNewPhotoToListing";
import FailedToAddNewPhotoToListing from "../events/FailedToAddNewPhotoToListing";
import {CommandsListingsPhotoApi} from "../../../data/api/listings";
import ProcessingAddNewPhotoToListing from "../events/ProcessingAddNewPhotoToListing.ts";
import uploadPhoto from "../../../data/uploadPhoto.ts";
import NewPhotoAddedToListing from "../events/NewPhotoAddedToListing.ts";

class HandleAddNewPhotoToListing extends Handler {

    public constructor(
        private readonly bus: Bus,
        private readonly photoApi: CommandsListingsPhotoApi
    ) {
        super();
    }

    public execute = async (command: AddNewPhotoToListing): Promise<void> => {

        try {

            const listingId = command.listingId;
            this.bus.publish(new ProcessingAddNewPhotoToListing(listingId)
                .withSender(command.senderId));

            const file = command.file;
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
                command.file
            );


            await this.photoApi.addPhotoToListing({
                listingId: listingId.val,
                addListingPhotoRequestBody: {
                    operationId: crypto.randomUUID(),
                    uploadedFile: response.objectPath
                }
            })

            this.bus.publish(new NewPhotoAddedToListing(listingId)
                .withSender(command.senderId));
        } catch (error) {
            this.bus.publish(new FailedToAddNewPhotoToListing(command.listingId, error)
                .withSender(command.senderId));
        }
    }

    private splitFilename = (fileName: string) => {
        const parts = fileName.split('.');
        const fileExtension = parts[parts.length - 1];
        const name = parts.slice(0, parts.length - 1).join('.');
        return {fileName: name, fileExtension};
    }
}

export default HandleAddNewPhotoToListing;