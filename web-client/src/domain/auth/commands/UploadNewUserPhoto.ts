import Command from "../../utils/Command";

class UploadNewUserPhoto extends Command {

    public constructor(public readonly file: File) {

        super();
    }
}

export default UploadNewUserPhoto;