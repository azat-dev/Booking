import Command from "../../../../domain/utils/Command.ts";
import Handler from "../../../../domain/utils/Handler.ts";
import OpenFileDialogForUploadingUserPhoto from "../../../commands/OpenFileDialogForUploadingUserPhoto.ts";
import OpenFileDialogForUploadingUserPhotoHandler from "../../../handlers/OpenFileDialogForUploadingUserPhotoHandler.ts";
import Bus from "../../../../domain/utils/Bus.ts";
import AppSession from "../../../../domain/auth/entities/AppSession.ts";

class ProfileCommandsHandlersProvider {

    private readonly handlers: Record<string, Handler>;

    public constructor(
        appSession: AppSession,
        bus: Bus
    ) {

        this.handlers = {
            [OpenFileDialogForUploadingUserPhoto.name]: new OpenFileDialogForUploadingUserPhotoHandler(appSession, bus),
        };
    }

    public getHandlerForCommand = (command: Command): Handler => {
        return this.handlers[command.constructor.name];
    }
}

export default ProfileCommandsHandlersProvider;