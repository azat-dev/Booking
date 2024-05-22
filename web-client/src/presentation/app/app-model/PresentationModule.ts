import OpenFileDialogForUploadingUserPhotoHandler from "../../handlers/OpenFileDialogForUploadingUserPhotoHandler.ts";
import Bus from "../../../domain/utils/Bus.ts";
import OpenFileDialogForUploadingUserPhoto from "../../commands/OpenFileDialogForUploadingUserPhoto.ts";
import AppSession from "../../../domain/auth/entities/AppSession.ts";

class PresentationModule {

    public constructor(
        private readonly appSession: AppSession,
        private readonly bus: Bus
    ) {
        this.registerCommandHandlers();
    }

    private registerCommandHandlers = (): void => {

        const handlersByCommands = {
            [OpenFileDialogForUploadingUserPhoto.type]: new OpenFileDialogForUploadingUserPhotoHandler(this.appSession, this.bus),
        };

        this.bus.subscribe(async command => {

            const handler = handlersByCommands[command.type];
            if (!handler) {
                return;
            }

            console.log(`%cPRESENTATION/HANDLER: %c${handler.type}`, 'color: #FF33FF; font-weight: bold;', command);
            handler.execute(command);
        });
    }
}

export default PresentationModule;