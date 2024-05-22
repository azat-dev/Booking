import Command from "../../utils/Command";
import UserId from "../values/UserId.ts";

class UploadNewUserPhoto extends Command {

    public constructor(
        public readonly userId: UserId,
        public readonly file: File
    ) {

        super();
    }
}

export default UploadNewUserPhoto;